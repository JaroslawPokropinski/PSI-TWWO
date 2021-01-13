export type Effect = {
  category: 'KNOWLEDGE' | 'SKILLS' | 'SOCIAL_COMPETENCES';
  code: string;
  createdAt: string;
  createdBy: {
    id: number;
    name: string;
    phoneNumber: string;
    surname: string;
  };
  description: string;
  id: number;
  isEngineerEffect: true;
  isLingualEffect: true;
  lastUpdatedAt: string;
  lastUpdatedBy: {
    id: number;
    name: string;
    phoneNumber: string;
    surname: string;
  };
  objectState: string;
  prkLevel: number;
  type: string;
};
