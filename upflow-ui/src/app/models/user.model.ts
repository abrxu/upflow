export enum DepartmentRole {
  MEMBER = 'MEMBER',
  MANAGER = 'MANAGER',
  LEAD = 'LEAD',
  CONTRIBUTOR = 'CONTRIBUTOR'
}

export interface UserAssociation {
  departmentId: number;
  role: DepartmentRole;
}

export interface UserCreationPayload {
  username: string;
  name: string;
  lastName: string;
  email: string;
  password: string;
  associations: UserAssociation[];
}

export interface LoginPayload {
  email: string;
  password: string;
}

export interface User {
  id: number;
  username: string;
  name: string;
  lastName: string;
  email: string;
  associations: UserAssociation[];
}

export interface LoginResponse {
  token: string;
  user?: {
    id: string;
    email: string;
    name: string;
  };
  expiresIn?: number;
}
