import React from 'react';
import { Button, Checkbox, Divider, Form, InputNumber, Select } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import ProgramContent from './ProgramContent';

const CardClasses: React.FunctionComponent<{ modify: boolean }> = ({
  modify = false,
}) => {
  return (
    <Form.List name="subjectClasses">
      {(fields, { add, remove }) => (
        <>
          <Divider />
          {fields.map((field) => (
            <div key={field.key}>
              <Form.Item
                label="typ"
                labelAlign="left"
                name={[field.name, 'subjectClassesType']}
                fieldKey={[field.fieldKey, 'subjectClassesType']}
                rules={[{ required: true, message: 'Wybierz forme zajęć!' }]}
              >
                <Select placeholder="Wybierz forme zajęć" disabled={!modify}>
                  <Select.Option value="LECTURE">Wykład</Select.Option>
                  <Select.Option value="CLASSES">Ćwiczenia</Select.Option>
                  <Select.Option value="PROJECT">Projekt</Select.Option>
                  <Select.Option value="SEMINAR">Seminarium</Select.Option>
                  <Select.Option value="LABORATORY">Laboratoria</Select.Option>
                </Select>
              </Form.Item>

              <Form.Item
                name={[field.name, 'creditingForm']}
                fieldKey={[field.fieldKey, 'creditingForm']}
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
                  <Select.Option value="EXAMINATION">Egzamin</Select.Option>
                  <Select.Option value="CREDITING_WITH_GRADE">
                    Ocena
                  </Select.Option>
                  {/* <Select.Option value="null">Nie dotyczy</Select.Option> */}
                </Select>
              </Form.Item>
              <Form.Item
                className="form-item"
                name={[field.name, 'ectsPoints']}
                fieldKey={[field.fieldKey, 'ectsPoints']}
                label="Punkty ECTS"
                labelAlign="left"
                hasFeedback
                rules={[{ required: true, message: 'Wybierz punkty ECTS!' }]}
              >
                <InputNumber min={0} disabled={!modify} />
              </Form.Item>
              <Form.Item
                className="form-item"
                name={[field.name, 'zzuHours']}
                fieldKey={[field.fieldKey, 'zzuHours']}
                label="Godziny ZZU"
                labelAlign="left"
                hasFeedback
                rules={[{ required: true, message: 'Wybierz godziny ZZU!' }]}
              >
                <InputNumber min={0} disabled={!modify} />
              </Form.Item>

              <Form.Item
                className="form-item"
                name={[field.name, 'cnpsHours']}
                fieldKey={[field.fieldKey, 'cnpsHours']}
                label="Godziny CNPS"
                labelAlign="left"
                hasFeedback
                rules={[{ required: true, message: 'Wybierz godziny CNPS!' }]}
              >
                <InputNumber min={0} disabled={!modify} />
              </Form.Item>

              <Form.Item
                className="form-item"
                name={[field.name, 'isFinalCourse']}
                fieldKey={[field.fieldKey, 'isFinalCourse']}
                valuePropName="checked"
                required
              >
                <Checkbox disabled={!modify}>
                  Czy końcowy dla grupy kursów
                </Checkbox>
              </Form.Item>

              <Form.Item
                className="form-item"
                name={[field.name, 'practicalEctsPoints']}
                fieldKey={[field.fieldKey, 'practicalEctsPoints']}
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
                <InputNumber min={0} disabled={!modify} />
              </Form.Item>

              <Form.Item
                className="form-item"
                name={[field.name, 'buEctsPoints']}
                fieldKey={[field.fieldKey, 'buEctsPoints']}
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
                <InputNumber min={0} disabled={!modify} />
              </Form.Item>
              <ProgramContent
                modify={modify}
                name={[field.name, 'program']}
                // fieldKey={[field.fieldKey, 'program']}
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
