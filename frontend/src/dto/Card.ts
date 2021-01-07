import { Effect } from './Effect';

export type Card = {
  createdAt: string;
  createdBy: {
    id: number;
    name: string;
    phoneNumber: string;
    surname: string;
  };
  educationalEffects: Effect[];
  fieldOfStudy: {
    faculty: number;
    id: number;
    name: string;
  };
  id: number;
  isGroupOfCourses: true;
  lastUpdatedAt: string;
  lastUpdatedBy: {
    id: number;
    name: string;
    phoneNumber: string;
    surname: string;
  };
  organisationalUnit: {
    id: number;
    name: string;
    type: 'FACULTY' | 'DEPARTMENT';
  };
  prerequisites: string[];
  primaryLiterature: string[];
  secondaryLiterature: string[];
  specialization: string;
  studiesForm: 'FULL_TIME' | 'PART_TIME';
  studiesLevel: 'FIRST' | 'SECOND' | 'UNIFORM_MAGISTER_STUDIES';
  subjectClasses: [
    {
      buEctsPoints: number;
      cnpsHours: number;
      creditingForm: 'EXAMINATION' | 'CREDITING_WITH_GRADE';
      ectsPoints: number;
      isFinalCourse: true;
      practicalEctsPoints: number;
      program: [
        {
          description: string;
          numberOfHours: number;
        }
      ];
      subjectClassesType:
        | 'LECTURE'
        | 'CLASSES'
        | 'LABORATORY'
        | 'PROJECT'
        | 'SEMINAR';
      zzuHours: number;
    }
  ];
  subjectCode: string;
  subjectName: string;
  subjectNameInEnglish: string;
  subjectObjectives: string[];
  subjectType: 'OBLIGATORY' | 'OPTIONAL' | 'UNIVERSITY_WIDE';
  supervisor: {
    id: number;
    name: string;
    phoneNumber: string;
    surname: string;
  };
  usedTeachingTools: string[];
};
