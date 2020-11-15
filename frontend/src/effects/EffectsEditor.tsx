import { Checkbox, Form, Input, InputNumber, Select } from 'antd';
import React, { useCallback, useMemo } from 'react';
import { useParams } from 'react-router-dom';
import EditorView from '../shared/EditorView';
import useQueryParam from '../shared/useQueryParam';
import EffectMappings from './EffectMappings';

import './EffectsEditor.css';

const EffectsEditorContent = ({ isArchive = false }) => {
  const { state } = useParams<{ state: string }>();
  const modify = useMemo(
    () => (state === 'create' || state === 'edit') && !isArchive,
    [state, isArchive]
  );
  const [code] = useQueryParam('code');

  return (
    <>
      <Form.Item
        className="effects-form-item"
        label="Kod"
        labelAlign="left"
        name="code"
        rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
      >
        <Input disabled={!(state === 'edit' && code === '')} />
      </Form.Item>

      <Form.Item
        className="form-item"
        label="Typ efektu"
        labelAlign="left"
        name="type"
        rules={[{ required: true, message: 'Wprowadź typ efektu!' }]}
      >
        <Select disabled={!modify}>
          <Select.Option value="minist">Ministerialny</Select.Option>
          <Select.Option value="kier">Kierunkowy</Select.Option>
          <Select.Option value="special">Specjalnościowy</Select.Option>
          <Select.Option value="przedm">Przedmiotowy</Select.Option>
        </Select>
      </Form.Item>

      <Form.Item
        className="effects-form-item"
        label="Opis"
        labelAlign="left"
        name="description"
        rules={[{ required: true, message: 'Wprowadź opis efektu!' }]}
      >
        <Input.TextArea disabled={!modify} />
      </Form.Item>

      <Form.Item
        name="category"
        label="Kategoria"
        labelAlign="left"
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
        labelAlign="left"
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
      <EffectMappings modify={modify} />
    </>
  );
};

function EffectsEditor(): JSX.Element {
  const [code] = useQueryParam('code');
  const onFinish = useCallback(
    (/* results */) => {
      // history.goBack();
    },
    []
  );

  return (
    <div className="effects-editor">
      <EditorView
        header="Efekt kształcenia"
        initialVals={{
          code,
          description: code
            ? 'zna podstawy dotyczące architektury systemu Linux i jego eksploatacji jako serwera lub stacji roboczej użytkownika w systemach informatycznych opartych o platformę Linux'
            : '',
          category: 'knowledge',
          prk: '6',
          bachelor: true,
          type: 'minist',
        }}
        useArchive
        archiveVals={{
          code,
          description: code
            ? 'zna podstawy dotyczące architektury systemu Linux i jego eksploatacji jako serwera lub stacji roboczej użytkownika w systemach informatycznych opartych o platformę Linux'
            : '',
          category: 'knowledge',
          prk: '6',
          bachelor: true,
          type: 'minist',
        }}
        name="effects"
        onFinish={onFinish}
        queryParams=""
      >
        <EffectsEditorContent />
        <EffectsEditorContent isArchive />
      </EditorView>
    </div>
  );
}

export default EffectsEditor;
