import Axios from 'axios';

const axios = Axios.create({
  baseURL: process.env.REACT_APP_SERVER_URL ?? 'http://localhost:8080/',
});

export const setAuthToken = (token: string): void => {
  axios.defaults.headers.common.Authorization = token;
};

export default axios;
