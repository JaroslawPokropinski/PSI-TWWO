import React, { useCallback, useContext } from 'react';
import { Form, Input, Button, Checkbox, message } from 'antd';
import { useHistory } from 'react-router-dom';
import axios from '../configuration/axios';
import AuthContext from '../context/AuthContext';

import './Login.css';
import Header from '../shared/Header';

function Login(): JSX.Element {
  const history = useHistory();
  const authContext = useContext(AuthContext);
  const onFinish = useCallback(
    (results) => {
      axios
        .post<string>('/api/user/signin', {
          username: results.username,
          password: results.password,
        })
        .then((res) => {
          authContext.token = res.data;
          history.replace('/home');
        })
        .catch((err) => {
          if (err.response && err.response.data.error) {
            message.error(`Login failed: ${err.response.data.error}`);
          } else {
            message.error(`Login failed: ${err.message}`);
          }
        });
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
