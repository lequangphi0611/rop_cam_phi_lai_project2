export enum Gender {

  FEMALE = 0,

  MALE = 1,

}

export const genders = [Gender.MALE, Gender.FEMALE];

export function parseGenderFromBoolean(gender: boolean) {
  return gender ? Gender.MALE : Gender.FEMALE;
}

export function toBoolean(gender: Gender): boolean {
  return gender === Gender.MALE;
}
