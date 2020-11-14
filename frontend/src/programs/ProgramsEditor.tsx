import { Form, Input, InputNumber, Select } from 'antd';
import React, { useCallback, useMemo } from 'react';
import { useHistory, useParams } from 'react-router-dom';
import EditorView from '../shared/EditorView';
import useQueryParam from '../shared/useQueryParam';
import ProgramBlocks from './ProgramBlocks';
import ProgramDisciplines from './ProgramDisciplines';
import ProgramEffects from './ProgramEffects';

import './ProgramsEditor.css';

const ProgramsEditorContent = ({ isArchive = false }) => {
  const [code] = useQueryParam('code');
  const { state } = useParams<{ state: string }>();
  const modify = useMemo(
    () => (state === 'create' || state === 'edit') && !isArchive,
    [state, isArchive]
  );

  return (
    <>
      <Form.Item
        className="form-item"
        label="Kod"
        labelAlign="left"
        name="code"
        rules={[{ required: true, message: 'Wprowadź kod programu uczenia!' }]}
      >
        <Input disabled={!(state === 'edit' && code === '')} />
      </Form.Item>
      {/* JednostkaOrganizacyjna */}
      <Form.Item
        className="cards-form-item"
        label="Jednostka organizacyjna"
        labelAlign="left"
        name="unit"
        rules={[
          {
            required: true,
            message: 'Wprowadź jednostkę organizacyjną (wydział)!',
          },
        ]}
      >
        <Select
          placeholder="Wprowadź jednostkę organizacyjną (wydział)"
          disabled={!modify}
        >
          <Select.Option value="w8">
            Wydział Informatyki i Zarządzania
          </Select.Option>
          <Select.Option value="w11">
            Wydział Podstawowych Problemów Techniki
          </Select.Option>
        </Select>
      </Form.Item>

      <Form.Item
        className="form-item"
        label="Nazwa kierunku"
        labelAlign="left"
        name="field"
        rules={[{ required: true, message: 'Wprowadź nazwe kierunku!' }]}
      >
        <Input placeholder="Wprowadź nazwe kierunku" disabled={!modify} />
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
      {/* poziomStudiów */}
      <Form.Item
        name="level"
        label="Poziom studiów"
        labelAlign="left"
        hasFeedback
        rules={[{ required: true, message: 'Wybierz poziom studiów!' }]}
      >
        <Select placeholder="Wybierz poziom studiów" disabled={!modify}>
          <Select.Option value="first">Pierwszego stopnia</Select.Option>
          <Select.Option value="second">Drugiego stopnia</Select.Option>
          <Select.Option value="single">Jednolite magisterski</Select.Option>
        </Select>
      </Form.Item>
      {/* formaStudiów */}
      <Form.Item
        name="form"
        label="Forma studiów"
        labelAlign="left"
        hasFeedback
        rules={[{ required: true, message: 'Wybierz formę studiów!' }]}
      >
        <Select placeholder="Wybierz formę studiów" disabled={!modify}>
          <Select.Option value="stationary">Studia stacjonarne</Select.Option>
          <Select.Option value="notStationary">
            Studia niestacjonarne
          </Select.Option>
        </Select>
      </Form.Item>

      <Form.Item
        name="title"
        label="Tytuł zawodowy"
        labelAlign="left"
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
        className="form-item"
        label="Liczba semestrów"
        labelAlign="left"
        name="periods"
        rules={[{ required: true, message: 'Wprowadź liczbę semestrów!' }]}
      >
        <InputNumber min={1} disabled={!modify} />
      </Form.Item>

      <Form.Item
        className="form-item"
        label="Łączna liczba godzin (CNPS)"
        labelAlign="left"
        name="hours"
        rules={[{ required: true, message: 'Wprowadź łączną liczbe godzin!' }]}
      >
        <InputNumber min={1} disabled={!modify} />
      </Form.Item>

      <Form.Item
        className="form-item"
        label="Liczba punktów ECTS"
        labelAlign="left"
        name="ects"
        rules={[{ required: true, message: 'Wprowadź liczbę punktów!' }]}
      >
        <InputNumber min={1} disabled={!modify} />
      </Form.Item>

      {/* wymaganiaWstępne */}
      <Form.Item
        className="form-item"
        label="Wymagania wstępne"
        labelAlign="left"
        name="requirements"
        rules={[{ required: true, message: 'Wprowadź wymagania wstępne!' }]}
      >
        <Input disabled={!modify} />
      </Form.Item>
      {/* sylwetkaAbsolwenta */}
      <Form.Item
        className="form-item"
        label="Sylwetka absolwenta"
        labelAlign="left"
        name="profile"
        rules={[{ required: true, message: 'Wprowadź sylwetkę absolwenta!' }]}
      >
        <Input disabled={!modify} />
      </Form.Item>
      {/* możliwośćKontynuacjiStudiów */}
      <Form.Item
        className="form-item"
        label="Możliwość kontynuacji studiów"
        labelAlign="left"
        name="continuation"
        rules={[
          {
            required: true,
            message: 'Wprowadź możliwość kontynuacji studiów!',
          },
        ]}
      >
        <Input disabled={!modify} />
      </Form.Item>
      {/* związekZMisjąIStrategiąRozwojuUczelni */}
      <Form.Item
        className="form-item"
        label="Związek z misją i strategią rozwoju uczelni"
        labelAlign="left"
        name="mission"
        rules={[
          {
            required: true,
            message: 'Wprowadź związek z misją i strategią rozwoju uczelni!',
          },
        ]}
      >
        <Input disabled={!modify} />
      </Form.Item>

      {/* BlokiZajęć */}
      <ProgramBlocks modify={modify} />
      {/* Programowe Efekty Kształcenia */}
      <ProgramEffects modify={modify} />
      {/* Dyscypliny */}
      <ProgramDisciplines modify={modify} />
    </>
  );
};

function ProgramsEditor(): JSX.Element {
  const [code] = useQueryParam('code');
  const onFinish = useCallback(
    (/* results */) => {
      // history.goBack();
    },
    [
      /* history */
    ]
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
      useArchive
      archiveVals={{
        code,
      }}
    >
      <ProgramsEditorContent />
      <ProgramsEditorContent isArchive />
    </EditorView>
  );
}

export default ProgramsEditor;
