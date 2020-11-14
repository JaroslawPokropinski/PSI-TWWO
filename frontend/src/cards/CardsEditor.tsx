import { Checkbox, Form, Input, InputNumber, Select } from 'antd';
import React, { useCallback, useMemo } from 'react';
import { useParams } from 'react-router-dom';

import './CardsEditor.css';
import useQueryParam from '../shared/useQueryParam';
import EditorView from '../shared/EditorView';
import CardGoals from './CardGoals';
import CardEffects from './CardEffects';
import CardRequirements from './CardRequirements';
import CardTools from './CardTools';
import CardClasses from './CardClasses';
import CardLiterature from './CardLiterature';

const mockedData = {
  descriptions: [{ pol: 'Przykładowy cel przedmiotu' }],
  code: 'INZ005234',
  angName: 'databases',
  caretaker: 'Stanisław Przykładowy',
  subjectType: 'notObligatory',
  form: 'stationary',
  degree: 1,
  fieldOfStudy: 'inf',
  unit: 'w8',
  tools: [{ tool: 'przykł narzędzie' }],
  classes: [
    {
      class: 'lecture',
      form: 'exam',
      ects: 1,
      zzu: 30,
      cnps: 30,
      p: 1,
      bk: 1,
      programContent: [{ content: 'treść programowa', hours: 30 }],
    },
  ],
};

function CardsEditor(): JSX.Element {
  const { state } = useParams<{ state: string }>();
  const [qname] = useQueryParam('name');
  const onFinish = useCallback(() => null, []);
  const modify = useMemo(() => state === 'create' || state === 'edit', [state]);

  return (
    <div className="cards-editor">
      <EditorView
        name="cards"
        initialVals={{
          name: qname,
          ...mockedData,
        }}
        onFinish={onFinish}
        queryParams={`?name=${qname}`}
        header="Edycja karty przedmiotu"
        isVerifiable
        isVerified={false}
      >
        <div>
          <Form.Item
            className="cards-form-item"
            label="Kod przedmiotu"
            labelAlign="left"
            name="code"
            rules={[{ required: true, message: 'Wprowadź kod przedmiotu!' }]}
          >
            <Input disabled={!(state === 'edit' && qname === '')} />
          </Form.Item>
          <Form.Item
            className="cards-form-item"
            label="Nazwa przedmiotu"
            labelAlign="left"
            name="name"
            rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
          >
            <Input disabled={!(state === 'edit' && qname === '')} />
          </Form.Item>
          <Form.Item
            className="cards-form-item"
            label="Ang. nazwa przedmiotu"
            labelAlign="left"
            name="angName"
            rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
          >
            <Input disabled={!modify} />
          </Form.Item>
          {/* Użytkownik */}
          <Form.Item
            className="form-item"
            label="Opiekun przedmiotu"
            labelAlign="left"
            name="caretaker"
            rules={[{ message: 'Wprowadź opiekuna przedmiotu!' }]}
          >
            <Input disabled={!modify} />
          </Form.Item>
          <Form.Item
            className="cards-form-item"
            label="Kierunek"
            labelAlign="left"
            name="fieldOfStudy"
            rules={[
              {
                required: false,
                message: 'Wprowadź kierunek!',
              },
            ]}
          >
            <Select placeholder="Wprowadź kierunek" disabled={!modify}>
              <Select.Option value="inf">Informatyka</Select.Option>
            </Select>
          </Form.Item>
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
          {/* rodzajPrzedmiotu */}
          <Form.Item
            className="form-item"
            label="Rodzaj przedmiotu"
            labelAlign="left"
            name="subjectType"
            rules={[
              {
                required: true,
                message: 'Wprowadź rodzaj przedmiotu!',
              },
            ]}
          >
            <Select placeholder="Wprowadź rodzaj przedmiotu" disabled={!modify}>
              <Select.Option value="obligatory">Obowiązkowy</Select.Option>
              <Select.Option value="notObligatory">Wybieralny</Select.Option>
              <Select.Option value="general">Ogólnouczelniany</Select.Option>
            </Select>
          </Form.Item>
          {/* formaStudiów */}
          <Form.Item
            name="form"
            label="Forma studiów"
            labelAlign="left"
            hasFeedback
            rules={[{ required: true, message: 'Wybierz forme studiów!' }]}
          >
            <Select placeholder="Wybierz forme studiów" disabled={!modify}>
              <Select.Option value="stationary">Stacjonarne</Select.Option>
              <Select.Option value="notStationary">
                Nie stacjonarne
              </Select.Option>
            </Select>
          </Form.Item>
          {/* stopień */}
          <Form.Item
            name="degree"
            label="Stopień"
            labelAlign="left"
            hasFeedback
            rules={[{ required: true, message: 'Wybierz stopień studiów!' }]}
          >
            <InputNumber min={1} max={2} disabled={!modify} />
          </Form.Item>
          {/* czyGrupaKursów */}
          <Form.Item className="form-item" name="group" valuePropName="checked">
            <Checkbox disabled={!modify}>Czy jest grupą kursów</Checkbox>
          </Form.Item>
          {/* Adding descriptions */}
          <CardGoals modify={modify} />
          {/* Adding effects */}
          <CardEffects modify={modify} />
          {/* wymaganieWstępne */}
          <CardRequirements modify={modify} />
          {/* narzędziaDydaktyczne */}
          <CardTools modify={modify} />
          {/* Zajęcia */}
          <CardClasses modify={modify} />
          {/* literatura */}
          <CardLiterature modify={modify} />
        </div>
      </EditorView>
    </div>
  );
}

export default CardsEditor;
