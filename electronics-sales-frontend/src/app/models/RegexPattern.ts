const PHONE_NUMBER_PATTERN = /^(0[0-9])+([0-9]{8,9})\b/i;

const EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]+$/i;

export const pattern = {
  phone: PHONE_NUMBER_PATTERN,
  email: EMAIL_PATTERN
}
