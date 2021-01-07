import { Checkbox, Form, Input, InputNumber, Select } from 'antd';
import React, {
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from 'react';
import { useHistory, useParams } from 'react-router-dom';

import './CardsEditor.css';
import { AxiosResponse } from 'axios';
import useQueryParam from '../shared/useQueryParam';
import EditorView from '../shared/EditorView';
import CardGoals from './CardGoals';
import CardEffects from './CardEffects';
import CardRequirements from './CardRequirements';
import CardTools from './CardTools';
import CardClasses from './CardClasses';
import CardLiterature from './CardLiterature';
import CardSecondaryLiterature from './CardSecondaryLiterature';
import { Card } from '../dto/Card';
import AuthContext from '../context/AuthContext';
import { LangContext } from '../context/LangContext';
import axios from '../configuration/axios';
import handleHttpError from '../shared/handleHttpError';

type FieldOfStudy = { id: number; name: string; faculty: number };
type OrganisationalUnit = { id: number; name: string; type: string };

const CardsEditorContent = ({ isArchive = false }) => {
  const auth = useContext(AuthContext);
  const lang = useContext(LangContext);

  const history = useHistory();
  const { state } = useParams<{ state: string }>();
  const [id] = useQueryParam('id');

  const [fields, setFields] = useState<FieldOfStudy[] | null>(null);
  const [units, setUnits] = useState<OrganisationalUnit[] | null>(null);

  const modify = useMemo(
    () => (state === 'create' || state === 'edit') && !isArchive,
    [state, isArchive]
  );

  useEffect(() => {
    axios
      .get<OrganisationalUnit[]>(`/api/organisational-units`, {
        headers: { Authorization: auth.token },
      })
      .then((res) => {
        setUnits(res.data);
      })
      .catch((err) => handleHttpError(err, history));
  }, [setUnits, auth, history]);

  useEffect(() => {
    axios
      .get<FieldOfStudy[]>(`/api/filed-of-study`, {
        headers: { Authorization: auth.token },
      })
      .then((res) => {
        setFields(res.data);
      })
      .catch((err) => handleHttpError(err, history));
  }, [setFields, auth, history]);

  return (
    <>
      {fields == null || units == null ? null : (
        <>
          <Form.Item
            className="cards-form-item"
            label={lang.getMessage('Subject code')}
            labelAlign="left"
            name="subjectCode"
            rules={[
              {
                required: true,
                message: lang.getMessage('Fill subject code!'),
              },
            ]}
          >
            <Input disabled={!(state === 'edit' && id === '')} />
          </Form.Item>
          <Form.Item
            className="cards-form-item"
            label={lang.getMessage('Subject name')}
            labelAlign="left"
            name="subjectName"
            rules={[
              {
                required: true,
                message: lang.getMessage('Fill subject name!'),
              },
            ]}
          >
            <Input disabled={!(state === 'edit' && id === '')} />
          </Form.Item>
          <Form.Item
            className="cards-form-item"
            label={lang.getMessage('Subject name in english')}
            labelAlign="left"
            name="subjectNameInEnglish"
            rules={[
              {
                required: true,
                message: lang.getMessage('Fill subject name in english!'),
              },
            ]}
          >
            <Input disabled={!modify} />
          </Form.Item>
          {/* Użytkownik */}
          {/* <Form.Item
        className="form-item"
        label="Opiekun przedmiotu"
        labelAlign="left"
        name="caretaker"
        rules={[{ message: 'Wprowadź opiekuna przedmiotu!' }]}
      >
        <Input disabled={!modify} />
      </Form.Item> */}
          <Form.Item
            className="cards-form-item"
            label={lang.getMessage('Field of study2')}
            labelAlign="left"
            name="fieldOfStudy"
            rules={[
              {
                required: true,
                message: lang.getMessage('Fill field of study!'),
              },
            ]}
          >
            <Select
              placeholder={lang.getMessage('Fill field of study!')}
              disabled={!modify}
            >
              {fields.map((f) => (
                <Select.Option key={f.id} value={f.id}>
                  {f.name}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item
            className="cards-form-item"
            label={lang.getMessage('Organisational unit')}
            labelAlign="left"
            name="organisationalUnit"
            rules={[
              {
                required: true,
                message: lang.getMessage('Fill organisationel unit!'),
              },
            ]}
          >
            <Select
              placeholder={lang.getMessage('Fill organisationel unit!')}
              disabled={!modify}
            >
              {units.map((u) => (
                <Select.Option key={u.id} value={u.id}>
                  {u.name}
                </Select.Option>
              ))}
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
              <Select.Option value="OBLIGATORY">Obowiązkowy</Select.Option>
              <Select.Option value="OPTIONAL">Wybieralny</Select.Option>
              <Select.Option value="UNIVERSITY_WIDE">
                Ogólnouczelniany
              </Select.Option>
            </Select>
          </Form.Item>
          {/* formaStudiów */}
          <Form.Item
            name="studiesForm"
            label="Forma studiów"
            labelAlign="left"
            hasFeedback
            rules={[{ required: true, message: 'Wybierz forme studiów!' }]}
          >
            <Select placeholder="Wybierz forme studiów" disabled={!modify}>
              <Select.Option value="FULL_TIME">Stacjonarne</Select.Option>
              <Select.Option value="PART_TIME">Nie stacjonarne</Select.Option>
            </Select>
          </Form.Item>
          {/* stopień */}
          <Form.Item
            name="studiesLevel"
            label="Stopień"
            labelAlign="left"
            hasFeedback
            rules={[{ required: true, message: 'Wybierz stopień studiów!' }]}
          >
            <Select placeholder="Wybierz stopień studiów" disabled={!modify}>
              <Select.Option value="FIRST">Pierwszego stopnia</Select.Option>
              <Select.Option value="SECOND">Drugiego stopnia</Select.Option>
              <Select.Option value="UNIFORM_MAGISTER_STUDIES">
                Jednolite magisterskie
              </Select.Option>
            </Select>
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
          {/* */}
          <CardSecondaryLiterature modify={modify} />
        </>
      )}
    </>
  );
};

function CardsEditor(): JSX.Element {
  const auth = useContext(AuthContext);
  const lang = useContext(LangContext);
  const history = useHistory();

  const [id] = useQueryParam('id');
  const isNew = useMemo(() => id === '', [id]);
  const [card, setCard] = useState<Card | null>(null);

  const onFinish = useCallback(
    async (d) => {
      const newCard = { code: d.subjectCode, ...d };
      // const effectPromses = (d.educationalEffectsId ?? []).map(
      //   (effectId: number) => {
      //     return axios
      //       .get<Card[]>(`/api/educational-effects/${effectId}`, {
      //         headers: { Authorization: auth.token },
      //       })
      //       .then((res) => {
      //         return res;
      //       })
      //       .catch((err) => handleHttpError(err, history));
      //   }
      // );

      // newCard.educationalEffects = await Promise.all(effectPromses).then(
      //   (ers: any) => {
      //     const efs: Effect[] = ers.map(
      //       (er: AxiosResponse<Effect[]>) => er.data
      //     );
      //     return efs;
      //   }
      // );
      // delete newCard.educationalEffectsId;

      // newCard.organisationalUnit =
      //   (
      //     await axios.get<OrganisationalUnit[]>(`/api/organisational-units`, {
      //       headers: { Authorization: auth.token },
      //     })
      //   ).data.filter(
      //     (unit) => unit.id === newCard.organisationalUnitId
      //   )?.[0] ?? null;
      // delete newCard.organisationalUnitId;

      // newCard.fieldOfStudy =
      //   (
      //     await axios.get<FieldOfStudy[]>(`/api/filed-of-study`, {
      //       headers: { Authorization: auth.token },
      //     })
      //   ).data.filter((f) => f.id === newCard.fieldOfStudyId)?.[0] ?? null;
      // delete newCard.fieldOfStudyId;

      // delete newCard.createdAt;
      // delete newCard.createdBy;
      // delete newCard.lastUpdatedAt;
      // delete newCard.lastUpdatedBy;

      if (isNew) {
        axios
          .post(`/api/subject-card`, [newCard], {
            headers: { Authorization: auth.token },
          })
          .then((res) => {
            setCard(res.data[0]);
          })
          .catch((err) => handleHttpError(err, history));
        return;
      }
      if (card == null) return;

      axios
        .put(`/api/subject-card`, [{ id: card.id, ...newCard }], {
          headers: { Authorization: auth.token },
        })
        .then((res) => {
          setCard(res.data[0]);
        })
        .catch((err) => handleHttpError(err, history));
    },
    [auth, history, card, isNew]
  );

  useEffect(() => {
    if (isNew) return;
    axios
      .get<Card[]>(`/api/subject-card/${id}`, {
        headers: { Authorization: auth.token },
      })
      .then((res) => {
        setCard(res.data[0]);
      })
      .catch((err) => handleHttpError(err, history));
  }, [id, isNew, history, auth]);

  const mapCard = useCallback((c: Card) => {
    return {
      ...c,
      organisationalUnit: c.organisationalUnit.id,
      fieldOfStudy: c.fieldOfStudy.id,
    };
  }, []);

  return (
    <div className="cards-editor">
      {card == null ? null : (
        <EditorView
          name="cards"
          initialVals={mapCard(card) ?? {}}
          onFinish={onFinish}
          queryParams=""
          header={lang.getMessage('Subject card')}
          isVerifiable
          isVerified={false}
          useArchive
          archiveVals={null ?? {}}
        >
          <CardsEditorContent />
          <CardsEditorContent isArchive />
        </EditorView>
      )}
    </div>
  );
}

export default CardsEditor;
