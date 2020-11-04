import { Button, Checkbox, Form, Input, InputNumber, Select } from 'antd';
import React, { useCallback, useState } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import RemoveModal from '../context/RemoveModal';
import Header from '../shared/Header';

import './ProgramsEditor.css';

function ProgramsEditor(): JSX.Element {
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
    <div className="programs-editor">
      <Header title="Edycja programu studiów" />
      <div className="programs-controlls">
        <Button
          className="programs-controlls-button"
          type="primary"
          onClick={() => history.goBack()}
        >
          Wstecz
        </Button>
        {query.get('state') !== 'create' ? (
          <Button
            className="programs-controlls-button"
            type="primary"
            onClick={() => setModify(true)}
            disabled={modify}
          >
            Modyfikuj
          </Button>
        ) : null}

        {query.get('state') === 'update' ? (
          <Button className="programs-remove-button" onClick={onRemove}>
            Usuń
          </Button>
        ) : null}
      </div>
      <Form
        className="programs-form"
        name="basic"
        initialValues={{
          code: query.get('code') ?? '',
        }}
        onFinish={onFinish}
      >
        <Form.Item
          className="programs-form-item"
          label="Kod"
          name="code"
          rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
        >
          <Input disabled={!(query.get('state') === 'create')} />
        </Form.Item>

        <Form.Item
          name="profil"
          label="Profil studiów"
          hasFeedback
          rules={[{ required: true, message: 'Wybierz profil studiów!' }]}
        >
          <Select placeholder="Wybierz profil studiów" disabled={!modify}>
            <Select.Option value="practical">Praktyczny</Select.Option>
            <Select.Option value="academic">Ogólnoakademicki</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          name="title"
          label="Tytuł zawodowy"
          hasFeedback
          rules={[{ required: true, message: 'Wybierz tytuł zawodowy!' }]}
        >
          <Select placeholder="Wybierz tytuł zawodowy" disabled={!modify}>
            <Select.Option value="engenieer">Inżynier</Select.Option>
            <Select.Option value="master">Magister</Select.Option>
            <Select.Option value="masterEngenieer">
              Magister inżynier
            </Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          name="form"
          label="Forma studiów"
          hasFeedback
          rules={[{ required: true, message: 'Wybierz forme studiów!' }]}
        >
          <Select placeholder="Wybierz forme studiów" disabled={!modify}>
            <Select.Option value="stationary">Stacjonarne</Select.Option>
            <Select.Option value="notStationary">Nie stacjonarne</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          name="language"
          label="Język prowadzenia"
          hasFeedback
          rules={[
            { required: true, message: 'Wybierz język prowadzenia studiów!' },
          ]}
        >
          <Select
            placeholder="Wybierz język prowadzenia studiów"
            disabled={!modify}
          >
            <Select.Option value="polish">Polski</Select.Option>
            <Select.Option value="english">Angielski</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          className="effects-form-item"
          label="Łączna liczba godzin"
          name="hours"
          rules={[
            { required: true, message: 'Wprowadź łączną liczbe godzin!' },
          ]}
        >
          <InputNumber min={1} disabled={!modify} />
        </Form.Item>

        <Form.Item className="programs-form-item">
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

export default ProgramsEditor;
