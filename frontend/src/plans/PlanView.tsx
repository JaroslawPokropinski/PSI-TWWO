import React, { useCallback, useMemo } from 'react';
import { useHistory, useParams } from 'react-router-dom';
import { Card, Collapse, Input, Form } from 'antd';
import EditorView from '../shared/EditorView';
import useQueryParam from '../shared/useQueryParam';

import './PlansEditor.css';
import PlanTable from './PlanTable';
import EctsDeficitTable from './EctsDeficitTable';
import TermCodeNameTable from './TermCodeNameTable';

const mockedData = {
  code: 'PO-W08-IST-IO- -ST-IIM-WRO-/2020L',
  unit: 'Wydział Informatyki i zarządzania',
  fieldOfStudy: 'Informatyka Stosowana',
  studyLevel: 'studia drugiego stopnia',
  studyForm: 'stacjonarne',
  specialization: 'Inżynieria oprogramowania',
  studyLanguage: 'Polski',
  enacted: '21-03-2019',
  inEffectSince: '26-02-2020',
};

const mockDataSem1Obligatory = [
  {
    number: 1,
    code: 'FZP008044W',
    name: 'Fizyczne podstawy współczesnej informatyki',
    w: 1,
    zzu: 15,
    cnps: 30,
    ects: 1,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 2,
    code: 'INZ002410L',
    name: 'Statystyka w zastosowaniach',
    l: 2,
    zzu: 30,
    cnps: 90,
    ects: 3,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 3,
    code: 'INZ002412L',
    name: 'Projekt i implementacja systemów webowych',
    l: 2,
    zzu: 30,
    cnps: 60,
    ects: 2,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 4,
    code: 'INZ002412W',
    name: 'Projekt i implementacja systemów webowych',
    l: 1,
    zzu: 30,
    cnps: 90,
    ects: 3,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 5,
    code: 'IZNFSDFSD',
    name: 'Zwinne wytwarzanie oprogramowania',
    w: 1,
    zzu: 15,
    cnps: 30,
    ects: 1,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 6,
    code: 'INZ002415S',
    name: 'Zwinne wytwarzanie oprogramowania',
    s: 1,
    zzu: 15,
    cnps: 60,
    ects: 2,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 7,
    code: 'INZ004456L',
    name: 'Programowanie współbieżne i funkcyjne',
    l: 2,
    zzu: 30,
    cnps: 90,
    ects: 3,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 8,
    code: 'INZ004456W',
    name: 'Programowanie współbieżne i funkcyjne',
    w: 2,
    zzu: 30,
    cnps: 60,
    ects: 2,
    creditioningForm: 'Egzamin',
  },
  {
    number: 9,
    code: 'MAT001656W',
    name: 'Metody planowania i analizy eksperymentów',
    w: 1,
    zzu: 15,
    cnps: 30,
    ects: 1,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 10,
    code: 'INZ002411Wc',
    name: 'Analiza biznesowa i systemowa',
    w: 1,
    c: 1,
    zzu: 30,
    cnps: 90,
    ects: 3,
    creditioningForm: 'Egzamin',
  },
  {
    number: 11,
    code: 'INZ002414Wp',
    name: 'Projekt bad.-rozw.w inż. oprog',
    w: 1,
    zzu: 15,
    cnps: 30,
    ects: 1,
    creditioningForm: 'Zaliczenie',
  },
];

const mockDataSem1Optional = [
  {
    number: 11,
    code: 'JZL100710BK',
    name: 'Języki obce KRK II st. (2ECTS)',
    w: 1,
    zzu: 45,
    cnps: 60,
    ects: 2,
    creditioningForm: 'Zaliczenie',
  },
];

const mockDataSem2Obligatory = [
  {
    number: 1,
    code: 'INZ002359P',
    name: 'Praca dyplomowa ',
    p: 2,
    zzu: 30,
    cnps: 60,
    ects: 2,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 2,
    code: 'INZ002417L',
    name: 'Technologie wspierającewytwarzanie oprogramowania',
    l: 2,
    zzu: 30,
    cnps: 90,
    ects: 3,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 3,
    code: 'INZ002417W',
    name: 'Technologie wspierającewytwarzanie oprogramowania',
    l: 2,
    zzu: 30,
    cnps: 60,
    ects: 2,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 4,
    code: 'INZ002412W',
    name: 'Projekt i implementacja systemów webowych',
    l: 1,
    zzu: 30,
    cnps: 90,
    ects: 3,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 5,
    code: 'IZNFSDFSD',
    name: 'Zwinne wytwarzanie oprogramowania',
    w: 1,
    zzu: 15,
    cnps: 30,
    ects: 1,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 6,
    code: 'INZ002415S',
    name: 'Zwinne wytwarzanie oprogramowania',
    s: 1,
    zzu: 15,
    cnps: 60,
    ects: 2,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 7,
    code: 'INZ004456L',
    name: 'Programowanie współbieżne i funkcyjne',
    l: 2,
    zzu: 30,
    cnps: 90,
    ects: 3,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 8,
    code: 'INZ004456W',
    name: 'Programowanie współbieżne i funkcyjne',
    w: 2,
    zzu: 30,
    cnps: 60,
    ects: 2,
    creditioningForm: 'Egzamin',
  },
  {
    number: 9,
    code: 'MAT001656W',
    name: 'Metody planowania i analizy eksperymentów',
    w: 1,
    zzu: 15,
    cnps: 30,
    ects: 1,
    creditioningForm: 'Zaliczenie',
  },
  {
    number: 10,
    code: 'INZ002411Wc',
    name: 'Analiza biznesowa i systemowa',
    w: 1,
    c: 1,
    zzu: 30,
    cnps: 90,
    ects: 3,
    creditioningForm: 'Egzamin',
  },
  {
    number: 11,
    code: 'INZ002414Wp',
    name: 'Projekt bad.-rozw.w inż. oprog',
    w: 1,
    zzu: 15,
    cnps: 30,
    ects: 1,
    creditioningForm: 'Zaliczenie',
  },
];

const mockDataSem2Optional = [
  {
    number: 11,
    code: 'JZL100710BK',
    name: 'Języki obce KRK II st. (2ECTS)',
    w: 1,
    zzu: 45,
    cnps: 60,
    ects: 2,
    creditioningForm: 'Zaliczenie',
  },
];

const mockEctsDeficit = [
  {
    term: 1,
    deficit: 8,
  },
  {
    term: 2,
    deficit: 8,
  },
];

const mockExams = [
  {
    term: 1,
    code: 'INZ002411Wc',
    name: 'Analiza biznesowa i systemowa',
  },
  {
    term: 1,
    code: 'INZ004456W',
    name: 'Progr. współbieżne i funkcyjne',
  },
  {
    term: 2,
    code: 'INZ003854W',
    name: 'Projektowanie sys. informat.',
  },
  {
    term: 2,
    code: 'INZ004457W',
    name: 'Bezpieczeństwo sys.web.i mob.',
  },
];

function PlanView(): JSX.Element {
  const history = useHistory();
  const [code] = useQueryParam('code');
  const { state } = useParams<{ state: string }>();
  const modify = useMemo(() => state === 'create' || state === 'edit', [state]);
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
