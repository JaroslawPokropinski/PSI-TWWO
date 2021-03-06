import React, { useCallback, useContext, useEffect, useMemo } from 'react';
import { Form, Input, Button } from 'antd';
import { useHistory } from 'react-router-dom';
import { useCookies } from 'react-cookie';
import axios from '../configuration/axios';
import AuthContext from '../context/AuthContext';
import { LangContext } from '../context/LangContext';

import './Login.css';
import Header from '../shared/Header';
import handleHttpError from '../shared/handleHttpError';

function Login(): JSX.Element {
  const history = useHistory();
  const authContext = useContext(AuthContext);
  const lang = useContext(LangContext);
  const [cookies, setCookie] = useCookies(['token']);
  const axiosOpts = useMemo(
    () => ({ headers: { Authorization: cookies.token } }),
    [cookies]
  );
  const onFinish = useCallback(
    (results) => {
      let token: string | null = null;
      axios
        .post<{ accessToken: string }>('/api/user/signin', {
          username: results.username,
          password: results.password,
        })
        .then((res) => {
          token = `Bearer ${res.data.accessToken}`;
          return axios.get('/api/user/current', {
            headers: { authorization: `Bearer ${res.data.accessToken}` },
          });
        })
        .then((res) => {
          setCookie('role', res.data.role, { path: '/' });
          setCookie('token', token, { path: '/' });
        })
        .catch((err) => handleHttpError(err));
    },
    [setCookie]
  );

  useEffect(() => {
    if (cookies.token == null || cookies.role == null) return;

    axios
      .get('/api/user/current', axiosOpts)
      .then(() => {
        authContext.token = cookies.token;
        authContext.role = cookies.role;

        history.replace('/home');
      })
      .catch(() => setCookie('token', null, { path: '/' }));
  }, [cookies.token, cookies.role, setCookie, authContext, history, axiosOpts]);

  return (
    <div className="Login">
      <Header title={lang.messages['app.title']} />
      <Form
        className="login-form"
        name="basic"
        initialValues={{ remember: false }}
        onFinish={onFinish}
      >
        <Form.Item
          className="login-form-item"
          label={lang.getMessage('User')}
          name="username"
          rules={[{ required: true, message: 'Please input your username!' }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          className="login-form-item"
          label={lang.getMessage('Password')}
          name="password"
          rules={[{ required: true, message: 'Please input your password!' }]}
        >
          <Input.Password />
        </Form.Item>

        {/* <Form.Item
          className="login-form-item"
          name="remember"
          valuePropName="checked"
        >
          <Checkbox>Remember me</Checkbox>
        </Form.Item> */}

        <Form.Item className="login-form-item">
          <Button type="primary" htmlType="submit">
            Log in
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
}

export default Login;
