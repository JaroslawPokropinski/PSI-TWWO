import Axios from 'axios';

const axios = Axios.create({
  baseURL: process.env.REACT_APP_SERVER_URL ?? 'http://localhost:8080/',
});

export default axios;
