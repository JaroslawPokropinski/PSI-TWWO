import { Button, Checkbox, Form, Input, InputNumber, Select } from 'antd';
import React, { useCallback, useState } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import RemoveModal from '../context/RemoveModal';
import Header from '../shared/Header';

import './EffectsEditor.css';

function EffectsEditor(): JSX.Element {
  const history = useHistory();
  const query = new URLSearchParams(useLocation().search);
  const onFinish = useCallback(
    (results) => {
      history.goBack();
    },
    [history]
  );
  const [isModalVisible, showModal] = useState(false);
  const onRemove = useCallback(() => {
    showModal(true);
  }, []);
  const onRemoveApprove = useCallback(() => {
    history.goBack();
  }, [history]);
  const onRemoveCancel = useCallback(() => {
    showModal(false);
  }, []);
  return (
    <div className="effects-editor">
      <Header title="Edycja efektu kształcenia" />
      <Form
        className="effects-form"
        name="basic"
        initialValues={{ code: query.get('code') ?? '' }}
        onFinish={onFinish}
      >
        <Form.Item
          className="effects-form-item"
          label="Kod"
          name="code"
          rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
        >
          <Input disabled={!(query.get('state') === 'create')} />
        </Form.Item>

        <Form.Item
          className="effects-form-item"
          label="Opis"
          name="description"
          rules={[{ required: true, message: 'Wprowadź opis efektu!' }]}
        >
          <Input.TextArea />
        </Form.Item>

        <Form.Item
          name="category"
          label="Kategoria"
          hasFeedback
          rules={[{ required: true, message: 'Wybierz kategorie efektu!' }]}
        >
          <Select placeholder="Wybierz kategorie efektu">
            <Select.Option value="practice">Praktyczny</Select.Option>
            <Select.Option value="academic">Ogólnouczelniany</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          className="effects-form-item"
          label="Poziom PRK"
          name="prk"
          rules={[{ required: true, message: 'Wprowadź poziom PRK efektu!' }]}
        >
          <InputNumber min={1} max={8} />
        </Form.Item>

        <Form.Item
          className="effects-form-item"
          name="bachelor"
          valuePropName="checked"
        >
          <Checkbox>Umożliwia inżnyniera</Checkbox>
        </Form.Item>

        <Form.Item
          className="effects-form-item"
          name="language"
          valuePropName="checked"
        >
          <Checkbox>Językowy</Checkbox>
        </Form.Item>

        <Form.Item className="effects-form-item">
          <Button type="primary" htmlType="submit">
            Zatwierdź
          </Button>
        </Form.Item>
        {query.get('state') === 'update' ? (
          <Form.Item className="effects-form-item">
            <Button className="effects-remove-button" onClick={onRemove}>
              Usuń
            </Button>
          </Form.Item>
        ) : null}
      </Form>
      <RemoveModal
        visible={isModalVisible}
        onOk={onRemoveApprove}
        onCancel={onRemoveCancel}
      />
    </div>
  );
}

export default EffectsEditor;
