import React, { useCallback, useContext } from 'react';
import { Form, Input, Button, Checkbox } from 'antd';
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
      // Commented for demonstration purposes
      // axios
      //   .post<string>('/login', {
      //     username: results.username,
      //     password: results.password,
      //   })
      //   .then((res) => {
      //     authContext.token = res.data;
      //     history.replace('/home');
      //   })
      //   .catch(() => {
      //     // TODO: Inform user
      //     // eslint-disable-next-line no-console
      //     console.error('Login failed!');
      //   });
      authContext.token = null;
      history.replace('/home');
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
