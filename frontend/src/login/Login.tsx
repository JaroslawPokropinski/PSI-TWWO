import React, { useCallback, useContext } from 'react';
import { Form, Input, Button, Checkbox } from 'antd';
import { useHistory } from 'react-router-dom';
import axios, { setAuthToken } from '../configuration/axios';
import AuthContext from '../context/AuthContext';

import './Login.css';
import Header from '../shared/Header';
import handleHttpError from '../shared/handleHttpError';

function Login(): JSX.Element {
  const history = useHistory();
  const authContext = useContext(AuthContext);
  const onFinish = useCallback(
    (results) => {
      axios
        .post<{ accessToken: string }>('/api/user/signin', {
          username: results.username,
          password: results.password,
        })
        .then((res) => {
          authContext.token = `Bearer ${res.data.accessToken}`;
          history.replace('/home');
        })
        .catch((err) => handleHttpError(err));
    },
    [history, authContext]
  );

  return (
    <div className="Login">
      <Header title="Kreator programów kształcenia PWR" />
      <Form
        className="login-form"
        name="basic"
        initialValues={{ remember: false }}
        onFinish={onFinish}
      >
        <Form.Item
          className="login-form-item"
          label="Username"
          name="username"
          rules={[{ required: true, message: 'Please input your username!' }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          className="login-form-item"
          label="Password"
          name="password"
          rules={[{ required: true, message: 'Please input your password!' }]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item
          className="login-form-item"
          name="remember"
          valuePropName="checked"
        >
          <Checkbox>Remember me</Checkbox>
        </Form.Item>

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
