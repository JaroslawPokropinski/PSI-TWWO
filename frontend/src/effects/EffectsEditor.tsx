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
  const [modify, setModify] = useState(query.get('state') === 'create');
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
      <div className="effects-controlls">
        <Button
          className="effects-controlls-button"
          type="primary"
          onClick={() => history.goBack()}
        >
          Wstecz
        </Button>
        {query.get('state') !== 'create' ? (
          <Button
            className="effects-controlls-button"
            type="primary"
            onClick={() => setModify(true)}
            disabled={modify}
          >
            Modyfikuj
          </Button>
        ) : null}

        {query.get('state') === 'update' ? (
          <Button className="effects-remove-button" onClick={onRemove}>
            Usuń
          </Button>
        ) : null}
      </div>
      <Form
        className="effects-form"
        name="basic"
        initialValues={{
          code: query.get('code') ?? '',
          description:
            'zna podstawy dotyczące architektury systemu Linux i jego eksploatacji jako serwera lub stacji roboczej użytkownika w systemach informatycznych opartych o platformę Linux',
          category: 'knowledge',
          prk: '6',
          bachelor: true,
        }}
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
          <Input.TextArea disabled={!modify} />
        </Form.Item>

        <Form.Item
          name="category"
          label="Kategoria"
          hasFeedback
          rules={[{ required: true, message: 'Wybierz kategorie efektu!' }]}
        >
          <Select placeholder="Wybierz kategorie efektu" disabled={!modify}>
            <Select.Option value="knowledge">Wiedza</Select.Option>
            <Select.Option value="skills">Umiejętności</Select.Option>
            <Select.Option value="social">Kompetencje społeczne</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          className="effects-form-item"
          label="Poziom PRK"
          name="prk"
          rules={[{ required: true, message: 'Wprowadź poziom PRK efektu!' }]}
        >
          <InputNumber min={1} max={8} disabled={!modify} />
        </Form.Item>

        <Form.Item
          className="effects-form-item"
          name="bachelor"
          valuePropName="checked"
        >
          <Checkbox disabled={!modify}>Umożliwia inżnyniera</Checkbox>
        </Form.Item>

        <Form.Item
          className="effects-form-item"
          name="language"
          valuePropName="checked"
        >
          <Checkbox disabled={!modify}>Językowy</Checkbox>
        </Form.Item>

        <Form.Item className="effects-form-item">
          {modify ? (
            <Button type="primary" htmlType="submit">
              Zatwierdź
            </Button>
          ) : null}
        </Form.Item>
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
