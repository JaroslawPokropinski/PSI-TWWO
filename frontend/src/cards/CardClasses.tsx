import React from 'react';
import { Button, Checkbox, Divider, Form, InputNumber, Select } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import ProgramContent from './ProgramContent';

const CardClasses: React.FunctionComponent<{ modify: boolean }> = ({
  modify = false,
}) => {
  return (
    <Form.List name="classes">
      {(fields, { add, remove }) => (
        <>
          <Divider />
          {fields.map((field) => (
            <div key={field.key}>
              <Form.Item
                label="typ"
                labelAlign="left"
                name={[field.name, 'class']}
                rules={[{ required: true, message: 'Wybierz forme zajęć!' }]}
              >
                <Select placeholder="Wybierz forme zajęć" disabled={!modify}>
                  <Select.Option value="lecture">Wykład</Select.Option>
                  <Select.Option value="exercise">Ćwiczenia</Select.Option>
                  <Select.Option value="project">Projekt</Select.Option>
                  <Select.Option value="seminary">Seminarium</Select.Option>
                  <Select.Option value="laboratory">Laboratoria</Select.Option>
                </Select>
              </Form.Item>

              <Form.Item
                name={[field.name, 'form']}
                label="Forma zaliczenia"
                labelAlign="left"
                hasFeedback
                rules={[
                  { required: true, message: 'Wybierz forme zaliczenia!' },
                ]}
              >
                <Select
                  placeholder="Wybierz forme zaliczenia"
                  disabled={!modify}
                >
                  <Select.Option value="exam">Egzamin</Select.Option>
                  <Select.Option value="mark">Ocena</Select.Option>
                  <Select.Option value="none">Nie dotyczy</Select.Option>
                </Select>
              </Form.Item>
              <Form.Item
                className="form-item"
                name={[field.name, 'ects']}
                label="Punkty ECTS"
                labelAlign="left"
                hasFeedback
                rules={[{ required: true, message: 'Wybierz punkty ECTS!' }]}
              >
                <InputNumber min={1} max={30} disabled={!modify} />
              </Form.Item>
              <Form.Item
                className="form-item"
                name={[field.name, 'zzu']}
                label="Godziny ZZU"
                labelAlign="left"
                hasFeedback
                rules={[{ required: true, message: 'Wybierz godziny ZZU!' }]}
              >
                <InputNumber min={1} max={900} disabled={!modify} />
              </Form.Item>

              <Form.Item
                className="form-item"
                name={[field.name, 'cnps']}
                label="Godziny CNPS"
                labelAlign="left"
                hasFeedback
                rules={[{ required: true, message: 'Wybierz godziny CNPS!' }]}
              >
                <InputNumber min={1} max={900} disabled={!modify} />
              </Form.Item>

              <Form.Item
                className="form-item"
                name={[field.name, 'ending']}
                valuePropName="checked"
              >
                <Checkbox disabled={!modify}>
                  Czy końcowy dla grupy kursów
                </Checkbox>
              </Form.Item>

              <Form.Item
                className="form-item"
                name={[field.name, 'p']}
                label="Punkty odp. zajęcią praktycznym"
                labelAlign="left"
                hasFeedback
                rules={[
                  {
                    required: true,
                    message: 'Wybierz punkty odp. zajęcią praktycznym!',
                  },
                ]}
              >
                <InputNumber min={1} max={900} disabled={!modify} />
              </Form.Item>

              <Form.Item
                className="form-item"
                name={[field.name, 'bk']}
                label="Punkty odp. zajęcią wymagającym bezpośredniego kontaktu"
                labelAlign="left"
                hasFeedback
                rules={[
                  {
                    required: true,
                    message:
                      'Wybierz punkty odp. zajęcią wymagającym bezpośredniego kontaktu!',
                  },
                ]}
              >
                <InputNumber min={1} max={900} disabled={!modify} />
              </Form.Item>
              <ProgramContent
                modify={modify}
                name={[field.name, 'programContent']}
              />

              {modify ? (
                <Button
                  type="dashed"
                  onClick={() => remove(field.name)}
                  icon={<MinusCircleOutlined />}
                >
                  Usuń typ zajęć
                </Button>
              ) : null}

              <Divider />
            </div>
          ))}
          {modify ? (
            <Form.Item>
              <Button
                type="dashed"
                onClick={() => add()}
                block
                icon={<PlusOutlined />}
              >
                Dodaj typ zajęć
              </Button>
            </Form.Item>
          ) : null}
        </>
      )}
    </Form.List>
  );
};

export default CardClasses;
