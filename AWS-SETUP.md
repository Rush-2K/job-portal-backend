# Deploy JobPortal to AWS EC2 — Step-by-step

> This guide walks you through deploying your Spring Boot + MySQL (Docker) application to an AWS EC2 instance. Copy this file as `deploy-ec2.md` into your repo or use it as-is.

---

## Prerequisites

- An AWS account and an IAM user with permission to create EC2 instances, security groups, and key pairs (do **not** use root for daily ops).
- Your project contains `Dockerfile`, `docker-compose.yml`, and a `.env` (kept out of git).
- Local machine with `ssh` and `scp` and the EC2 private key (`.pem`).
- Docker and Docker Compose knowledge (basic).

---

## Quick overview (high level)

1. Create an EC2 instance (choose AMI & key pair).
2. Create a Security Group that allows SSH (22) and your app port (8080) — restrict SSH to trusted IPs.
3. SSH into the instance, install Docker & Docker Compose.
4. Upload your project (scp or git clone) and the `.env` file to the instance.
5. Start containers with `docker-compose up --build -d`.
6. Verify and (optionally) assign an Elastic IP, enable logs and monitoring.

---

## 1 — Create resources in AWS

### 1.1 Create a key pair (if you don't have one)

1. AWS Console → EC2 → Key Pairs → Create key pair.
2. Download the `.pem` file and store it securely (e.g. `~/aws-keys/jbportal-key.pem`).
3. Set permissions locally:

```bash
chmod 400 ~/aws-keys/jbportal-key.pem
```

### 1.2 Create a Security Group

- Name: `jobportal-sg`
- Inbound rules:

  - **SSH** (22) → Source: your public IP or devs’ IPs (or temporarily `0.0.0.0/0` for testing)
  - **App** (8080) → Source: `0.0.0.0/0` (or restricted range behind a reverse proxy)

- Outbound: allow all (default)

> **Note:** Do **not** open MySQL (3306) to the world. Allow it only from trusted IPs or keep MySQL internal.

### 1.3 Launch EC2 instance

- EC2 → Launch instance

  - AMI: Amazon Linux 2023 or Ubuntu 22.04 LTS (both fine)
  - Instance type: `t2.micro` (free tier) / `t3.small` for more CPU
  - Key pair: select the `.pem` you created
  - Security group: attach `jobportal-sg`
  - Storage: 8–20 GB (increase if DB large)

Record the **Public IPv4** address.

---

## 2 — SSH to the server

```bash
ssh -i ~/aws-keys/jbportal-key.pem ec2-user@<EC2_PUBLIC_IP>
# ubuntu AMI => ssh -i ~/aws-keys/jbportal-key.pem ubuntu@<EC2_PUBLIC_IP>
```

If prompted `Are you sure you want to continue connecting?` type `yes`.

Confirm you are inside:

```bash
whoami
pwd
uname -a
```

---

## 3 — Install Docker & Docker Compose on EC2

### Amazon Linux 2023 (DNF):

```bash
sudo dnf update -y
sudo dnf install -y docker
sudo systemctl enable --now docker
sudo usermod -aG docker $USER
# Install docker-compose binary
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

### Ubuntu 22.04 (APT):

```bash
sudo apt update && sudo apt upgrade -y
sudo apt install -y docker.io docker-compose
sudo systemctl enable --now docker
sudo usermod -aG docker $USER
```

> Log out and log back in (or `exit` then ssh again) so your group membership is updated.

Check:

```bash
docker --version
docker-compose --version
```

---

## 4 — Prepare your project for deployment

### 4.1 Remove secrets from repo (if any)

- Ensure `.env` is in `.gitignore`.
- Use placeholder values in `docker-compose.yml`, and populate a `.env` file on the server.

### 4.2 Example `.env` (on the EC2 server)

```env
MYSQL_ROOT_PASSWORD=supersecret
MYSQL_DATABASE=jobportal
MYSQL_USER=jobuser
MYSQL_PASSWORD=jobpass
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/jobportal
SPRING_DATASOURCE_USERNAME=jobuser
SPRING_DATASOURCE_PASSWORD=jobpass
```

### 4.3 Recommended `docker-compose.yml` snippet

```yaml
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: ${MYSQL_DATABASE}
      MYSQL_USER: ${MYSQL_USER}
      MYSQL_PASSWORD: ${MYSQL_PASSWORD}
    volumes:
      - mysql_data:/var/lib/mysql
      - ./db-init/jobportal.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "3306:3306"

  app:
    build:
      context: .
      dockerfile: Dockerfile
    environment:
      SPRING_DATASOURCE_URL: ${SPRING_DATASOURCE_URL}
      SPRING_DATASOURCE_USERNAME: ${SPRING_DATASOURCE_USERNAME}
      SPRING_DATASOURCE_PASSWORD: ${SPRING_DATASOURCE_PASSWORD}
    ports:
      - "8080:8080"
    depends_on:
      - mysql

volumes:
  mysql_data:
```

> If you don't want to expose MySQL externally, remove the `3306:3306` port mapping and use internal container networking.

---

## 5 — Upload project files to EC2

Two common methods:

### Option A — `scp` from your local machine

```bash
scp -i ~/aws-keys/jbportal-key.pem -r /path/to/jobportal-api ec2-user@<EC2_PUBLIC_IP>:/home/ec2-user/
```

### Option B — `git clone` on the server

```bash
# create SSH deploy key or use https if private
git clone https://github.com/<you>/<repo>.git
```

Create the `.env` on the server inside the project folder:

```bash
cd ~/jobportal-api
nano .env
# paste env values
```

---

## 6 — Build & Run with Docker Compose

From the project root on EC2:

```bash
# ensure the JAR is built locally or CI — if using docker build with Maven inside image, run maven first
mvn clean package -DskipTests

# then (option A) build image via docker-compose
docker-compose up --build -d

# or (option B) if image prebuilt/pulled
docker-compose up -d
```

Check containers:

```bash
docker ps
```

View logs (follow):

```bash
docker-compose logs -f app
```

Test the app externally:

```bash
curl http://<EC2_PUBLIC_IP>:8080/actuator/health
```

If the response is `{"status":"UP"}` your app is reachable.

---

## 7 — Post-deploy steps (recommended)

### 7.1 Assign Elastic IP (persistent IP)

- EC2 Console → Network & Security → Elastic IPs → Allocate Elastic IP → Associate with instance.

### 7.2 Setup a reverse proxy (optional but recommended)

- Install Nginx to forward traffic from 80/443 to 8080 and handle TLS (Let's Encrypt certs).

Example Nginx site config (proxy to Spring Boot):

```nginx
server {
  listen 80;
  server_name your.domain.com;

  location / {
    proxy_pass http://127.0.0.1:8080;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  }
}
```

Use Certbot for HTTPS:

```bash
sudo apt install certbot python3-certbot-nginx
sudo certbot --nginx -d your.domain.com
```

### 7.3 Run containers as a service (systemd)

Create `/etc/systemd/system/jobportal.service` to automatically start:

```ini
[Unit]
Description=JobPortal Docker Compose
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
WorkingDirectory=/home/ec2-user/jobportal-api
ExecStart=/usr/local/bin/docker-compose up -d
ExecStop=/usr/local/bin/docker-compose down
RemainAfterExit=yes

[Install]
WantedBy=multi-user.target
```

Enable service:

```bash
sudo systemctl daemon-reload
sudo systemctl enable jobportal.service
sudo systemctl start jobportal.service
```

### 7.4 Backups & persistence

- MySQL data stored in `mysql_data` volume — periodically `mysqldump` and push to S3:

```bash
docker exec jobportal-mysql sh -c 'exec mysqldump -u$MYSQL_USER -p"$MYSQL_PASSWORD" $MYSQL_DATABASE' > backup-$(date +%F).sql
aws s3 cp backup-$(date +%F).sql s3://your-bucket/backups/
```

### 7.5 Logs & monitoring

- Enable Spring Actuator and expose only `health` & `metrics` publicly.
- Consider pushing logs to CloudWatch or ELK and metrics to Prometheus/Grafana.

---

## 8 — Troubleshooting checklist

- App fails to start: `docker-compose logs -f app` and `docker logs jobportal-api`.
- DB connection errors: confirm `SPRING_DATASOURCE_URL` and that `mysql` container is Up (`docker ps`) and DB credentials match.
- Port in use: another process on 8080 — use `ss -tulpn | grep 8080`.
- Firewall / Security group: confirm inbound rules allow access.

---

## 9 — Tear down / save costs

- To stop containers but keep images/volumes: `docker-compose stop`
- To stop and remove containers/networks (keep images & volumes): `docker-compose down`
- To remove containers and volumes (data lost): `docker-compose down -v`
- To stop EC2 (save compute costs): stop the instance in AWS Console (EBS storage still billed)
- To delete everything (no cost): terminate the EC2 instance and delete EBS volumes & snapshots

---

## 10 — Optional: push images to ECR & run with ECS (next step)

If you want a more production-ready flow, build images, push to Amazon ECR, and deploy to ECS (Fargate) or EKS. That lets you avoid managing EC2 instances directly and integrates with CI/CD.

---

## Appendix — Useful commands reference

```bash
# SSH
ssh -i ~/aws-keys/jbportal-key.pem ec2-user@<EC2_PUBLIC_IP>

# Copy project to EC2
scp -i ~/aws-keys/jbportal-key.pem -r /local/path/jobportal-api ec2-user@<EC2_PUBLIC_IP>:/home/ec2-user/

# Start docker-compose
docker-compose up --build -d

# View logs
docker-compose logs -f app

# Stop but keep images
docker-compose stop

# Remove containers
docker-compose down

# Remove containers + volumes
docker-compose down -v

# Backup DB
docker exec jobportal-mysql mysqldump -u jobuser -pjobpass jobportal > backup.sql
```

---

Happy deploying! If you want, I can also create a shorter **one-page checklist** that you can paste into your repo README (or generate a script to automate these steps).
