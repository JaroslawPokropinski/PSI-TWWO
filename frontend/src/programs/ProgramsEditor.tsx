import { Button, Form, Input, InputNumber, Select } from 'antd';
import React, { useCallback, useMemo } from 'react';
import { useHistory, useParams } from 'react-router-dom';
import EditorView from '../shared/EditorView';
import useQueryParam from '../shared/useQueryParam';

import './ProgramsEditor.css';

function ProgramsEditor(): JSX.Element {
  const history = useHistory();
  const [code] = useQueryParam('code');
  const { state } = useParams<{ state: string }>();
  const modify = useMemo(() => state === 'create' || state === 'edit', [state]);
  const onFinish = useCallback(
    (_results) => {
      history.goBack();
    },
    [history]
  );

  return (
    <EditorView
      name="programs"
      initialVals={{
        code,
      }}
      onFinish={onFinish}
      queryParams={`?code=${code}`}
      header="Edycja programów uczenia"
    >
      <Form.Item
        className="form-item"
        label="Kod"
        labelAlign="left"
        name="code"
        rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
      >
        <Input disabled={!(state === 'create')} />
      </Form.Item>

      <Form.Item
        name="profil"
        label="Profil studiów"
        labelAlign="left"
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
        labelAlign="left"
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
        labelAlign="left"
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
        labelAlign="left"
        name="hours"
        rules={[{ required: true, message: 'Wprowadź łączną liczbe godzin!' }]}
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
    </EditorView>
  );
}

export default ProgramsEditor;
