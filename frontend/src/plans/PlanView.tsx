import React, { useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { Card, Collapse, Input, Form } from 'antd';
import EditorView from '../shared/EditorView';
import useQueryParam from '../shared/useQueryParam';

import './PlansEditor.css';
import PlanTable from './PlanTable';
import EctsDeficitTable from './EctsDeficitTable';
import TermCodeNameTable from './TermCodeNameTable';
import {
  mockDataSem1Obligatory,
  mockDataSem1Optional,
  mockDataSem2Obligatory,
  mockDataSem2Optional,
  mockEctsDeficit,
  mockedData,
  mockExams,
} from './mocks';

function PlanView(): JSX.Element {
  // const history = useHistory();
  const [code] = useQueryParam('code');
  const { state } = useParams<{ state: string }>();
  // const modify = useMemo(() => state === 'create' || state === 'edit', [state]);
  const onFinish = useCallback(
    (/* results */) => {
      // history.goBack();
    },
    [
      /* history */
    ]
  );

  return (
    <div className="plans">
      <EditorView
        name="programs"
        initialVals={{
          ...mockedData,
        }}
        onFinish={onFinish}
        queryParams={`?code=${code}`}
        header="Plan studiów"
      >
        <Card title="Informacje ogólne">
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
            className="effects-form-item"
            label="Wydział"
            labelAlign="left"
            name="unit"
            rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
          >
            <Input disabled={!(state === 'edit' && code === '')} />
          </Form.Item>
          <Form.Item
            className="effects-form-item"
            label="Kierunek"
            labelAlign="left"
            name="fieldOfStudy"
            rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
          >
            <Input disabled={!(state === 'edit' && code === '')} />
          </Form.Item>
          <Form.Item
            className="effects-form-item"
            label="Stopień"
            labelAlign="left"
            name="studyLevel"
            rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
          >
            <Input disabled={!(state === 'edit' && code === '')} />
          </Form.Item>
          <Form.Item
            className="effects-form-item"
            label="Forma"
            labelAlign="left"
            name="studyForm"
            rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
          >
            <Input disabled={!(state === 'edit' && code === '')} />
          </Form.Item>
          <Form.Item
            className="effects-form-item"
            label="Specjalizacja"
            labelAlign="left"
            name="specialization"
            rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
          >
            <Input disabled={!(state === 'edit' && code === '')} />
          </Form.Item>
          <Form.Item
            className="effects-form-item"
            label="Język studiów"
            labelAlign="left"
            name="studyLanguage"
            rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
          >
            <Input disabled={!(state === 'edit' && code === '')} />
          </Form.Item>
          <Form.Item
            className="effects-form-item"
            label="Uchwalony dnia"
            labelAlign="left"
            name="enacted"
            rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
          >
            <Input disabled={!(state === 'edit' && code === '')} />
          </Form.Item>
          <Form.Item
            className="effects-form-item"
            label="Obowiązuje od"
            labelAlign="left"
            name="inEffectSince"
            rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
          >
            <Input disabled={!(state === 'edit' && code === '')} />
          </Form.Item>
        </Card>
      </EditorView>
      <Card title="Zestaw kursów obowiązkowych i wybieralnych w układzie semestralnym">
        <Collapse>
          <Collapse.Panel header="Semestr 1" key="1">
            <Card type="inner" title="Kursy obowiązkowe">
              <PlanTable data={mockDataSem1Obligatory} />
            </Card>
            <Card type="inner" title="Kursy wybieralne">
              <PlanTable data={mockDataSem1Optional} />
            </Card>
          </Collapse.Panel>
          <Collapse.Panel header="Semestr 2" key="2">
            <Card type="inner" title="Kursy obowiązkowe">
              <PlanTable data={mockDataSem2Obligatory} />
            </Card>
            <Card type="inner" title="Kursy wybieralne">
              <PlanTable data={mockDataSem2Optional} />
            </Card>
          </Collapse.Panel>
          <Collapse.Panel header="Semestr 3" key="3">
            <Card type="inner" title="Kursy obowiązkowe">
              <PlanTable data={mockDataSem1Obligatory} />
            </Card>
            <Card type="inner" title="Kursy wybieralne">
              <PlanTable data={mockDataSem1Optional} />
            </Card>
          </Collapse.Panel>
        </Collapse>
      </Card>
      <Card title="Zestaw kursów przeznaczonych do realizacji w trybie zdalnego nauczania">
        <TermCodeNameTable data={[]} />
      </Card>
      <Card title="Zestaw egzaminów w układzie semestralnym">
        <TermCodeNameTable data={mockExams} />
      </Card>
      <Card title="Deficyt punktów dopuszczalny na poszczególnych semestrach">
        <EctsDeficitTable data={mockEctsDeficit} />
      </Card>
    </div>
  );
}

export default PlanView;
