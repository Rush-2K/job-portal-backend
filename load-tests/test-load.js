import http from 'k6/http';
import { sleep, check } from 'k6';

export let options = {
  vus: 100,            // 10 virtual users
  duration: '60s',    // run for 30 seconds
};

export default function () {
  let res = http.get('http://localhost:8080/api/jobs/view'); // adjust endpoint as needed

  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  sleep(1); // wait 1 second between requests per user
}
