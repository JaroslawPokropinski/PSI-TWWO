import React from 'react';
import Title from 'antd/lib/typography/Title';
import { Form, Input, Button, Checkbox } from 'antd';

import logo from './resources/logo.png';
import './App.css';

function App(): JSX.Element {
  return (
    <div className="App">
      <div className="Login">
        <header>
          <img className="head-logo" src={logo} alt="Logo PWR" />

          <Title className="head-text">Kreator programów krztałcenia PWR</Title>
        </header>
        <Form
          className="login-form"
          name="basic"
          initialValues={{ remember: true }}
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
