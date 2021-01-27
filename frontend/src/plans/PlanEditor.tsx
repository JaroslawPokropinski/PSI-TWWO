import { Card as AntCard, DatePicker, Form, Input, Select } from 'antd';
import React, {
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from 'react';
import { useHistory, useParams } from 'react-router-dom';
import moment from 'moment';
import axios from '../configuration/axios';
import AuthContext from '../context/AuthContext';
import { LangContext } from '../context/LangContext';
import { FieldOfStudy } from '../dto/FieldOfStudy';
import EditorView from '../shared/EditorView';
import handleHttpError from '../shared/handleHttpError';
import useQueryParam from '../shared/useQueryParam';
import { VersionHistory } from '../shared/versionHistory';

import { Plan } from '../dto/Plan';
import { SemesterEditor } from './SemesterEditor';
import { Card } from '../dto/Card';
import { ProgramPicker } from './ProgramPicker';
import { Program } from '../dto/Program';
import { Semester } from '../dto/Semester';

const ProgramsEditorContent = ({
  isArchive = false,
  initCards = new Array<Card>(),
  initPrograms = new Array<Program>(),
}) => {
  const auth = useContext(AuthContext);
  const lang = useContext(LangContext);
  const history = useHistory();

  const { state } = useParams<{ state: string }>();
  const axiosOpts = useMemo(
    () => ({ headers: { Authorization: auth.token } }),
    [auth]
  );
  const modify = useMemo(
    () => (state === 'create' || state === 'edit') && !isArchive,
    [state, isArchive]
  );

  const [fields, setFields] = useState<FieldOfStudy[]>([]);

  useEffect(() => {
    axios
      .get<FieldOfStudy[]>(`/api/field-of-study`, axiosOpts)
      .then((res) => {
        setFields(res.data);
      })
      .catch((err) => handleHttpError(err, history));
  }, [axiosOpts, history]);

  return (
    <>
      <AntCard>
        <Form.Item
          className="form-item"
          label="Kod"
          labelAlign="left"
          name="code"
          rules={[
            { required: true, message: 'Wprowadź kod programu uczenia!' },
          ]}
        >
          <Input disabled={!(state === 'edit')} />
        </Form.Item>

        <Form.Item
          className="cards-form-item"
          label={lang.getMessage('In effect since')}
          labelAlign="left"
          name="inEffectSince"
          rules={[
            {
              required: true,
              message: 'Wprowadź jednostkę organizacyjną (wydział)!',
            },
          ]}
        >
          <DatePicker />
        </Form.Item>
      </AntCard>
      <AntCard title={lang.getMessage('Program')}>
        <ProgramPicker modify={modify} initPrograms={initPrograms} />
      </AntCard>
      <AntCard title={lang.getMessage('Semesters')}>
        <SemesterEditor modify={modify} initCards={initCards} />
      </AntCard>
    </>
  );
};

function PlanEditor(): JSX.Element {
  const auth = useContext(AuthContext);
  const history = useHistory();

  const [id] = useQueryParam('id');
  const isNew = useMemo(() => id === '', [id]);
  const [plan, setPlan] = useState<Plan | null>(null);
  const axiosOpts = useMemo(
    () => ({ headers: { Authorization: auth.token } }),
    [auth]
  );

  const onFinish = useCallback(
    (d) => {
      const newPlan = {
        ...d,
        decreeDate: moment.utc().format(),
        studiesProgramId: d.studiesProgramId[0],
      };
      (isNew || plan == null
        ? axios.post(`/api/studies-plan`, [newPlan], axiosOpts)
        : axios.put(
            `/api/studies-plan`,
            [{ id: plan.id, ...newPlan }],
            axiosOpts
          )
      )
        .then((res) =>
          axios.get<Plan[]>(`/api/studies-plan/${res.data[0].id}`, axiosOpts)
        )
        .then((res) => {
          setPlan(res.data[0]);
          return axios.post(
            `/api/semester`,
            d.semesters.map((s: any, n: number) => ({
              ...s,
              number: n + 1,
              studiesPlanId: res.data[0].id,
            })),
            axiosOpts
          );
        })
        .then(() => {
          history.goBack();
        })
        .catch((err) => handleHttpError(err, history));
    },
    [axiosOpts, history, plan, isNew]
  );

  useEffect(() => {
    if (isNew) return;
    axios
      .get<Plan[]>(`/api/studies-plan/${id}`, axiosOpts)
      .then((res) => {
        setPlan(res.data[0]);
      })
      .catch((err) => handleHttpError(err, history));
  }, [id, isNew, history, axiosOpts]);

  const [initPrograms, setInitPrograms] = useState<Program[] | null>([]);
  useEffect(() => {
    if (isNew || plan == null) return;
    axios
      .get<Program[]>(
        `/api/studies-program/${plan.studiesProgramId}`,
        axiosOpts
      )
      .then((res) => {
        setInitPrograms(res.data);
      })
      .catch((err) => handleHttpError(err, history));
  }, [id, isNew, history, axiosOpts, plan]);

  const [semesters, setSemesters] = useState<Semester[] | null>(null);
  useEffect(() => {
    if (isNew) return;
    axios
      .get<Semester[]>(`/api/semester/studies-plan/${id}`, axiosOpts)
      .then((res) => {
        setSemesters(res.data);
      })
      .catch((err) => handleHttpError(err, history));
  }, [id, isNew, history, axiosOpts]);

  const mapPlan = useCallback(
    (p: Plan) => {
      try {
        return {
          ...p,
          studiesProgramId: [p.studiesProgramId],
          inEffectSince: moment(p.inEffectSince),
          semesters,
        };
      } catch (e) {
        return {};
      }
    },
    [semesters]
  );

  return (
    <div>
      {(plan == null || initPrograms == null || semesters == null) &&
      !isNew ? null : (
        <EditorView
          name="plans"
          mapper={mapPlan}
          initialVals={plan ?? {}}
          onFinish={onFinish}
          queryParams=""
          header="Plany studiów"
          isVerifiable
          isVerified={false}
        >
          <ProgramsEditorContent
            initCards={[]}
            initPrograms={initPrograms ?? []}
          />
        </EditorView>
      )}
    </div>
  );
}

export default PlanEditor;
