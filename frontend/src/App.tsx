import React, { useCallback } from 'react';
import Title from 'antd/lib/typography/Title';
import { Form, Input, Button, Checkbox } from 'antd';

import logo from './resources/logo.png';
import './App.css';
import axios from './configuration/axios';

function App(): JSX.Element {
  const onFinish = useCallback((results) => {
    axios
      .post('/api/user/signin', {
        username: results.username,
        password: results.password,
      })
      .then(() => {
        alert('Login succesfull.');
      })
      .catch(() => {
        alert('Login failed!');
      });
  }, []);

  return (
    <div className="App">
      <div className="Login">
        <header>
          <img className="head-logo" src={logo} alt="Logo PWR" />

          <Title className="head-text">Kreator programów kształcenia PWR</Title>
        </header>
        <Form
          className="login-form"
          name="basic"
          initialValues={{ remember: true }}
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
    </div>
  );
}

export default App;