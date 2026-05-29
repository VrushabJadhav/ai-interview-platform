const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

export type Role = 'CANDIDATE' | 'RECRUITER' | 'ADMIN';

export type AuthResponse = {
  token: string;
  userId: string;
  email: string;
  role: Role;
};

export type InterviewResponse = {
  id: string;
  candidateId: string;
  role: string;
  status: 'CREATED' | 'SUBMITTED';
  createdAt: string;
  submittedAt?: string | null;
};

export type EvaluationResult = {
  id: string;
  interviewId: string;
  candidateId: string;
  correctness: number;
  clarity: number;
  depth: number;
  communication: number;
  summary: string;
  createdAt: string;
};

export type CheckoutResponse = {
  paymentId: string;
  checkoutSessionId: string;
  checkoutUrl: string;
};

export type PaymentResponse = {
  paymentId: string;
  status: 'PENDING' | 'COMPLETED';
  amount: number;
  currency: string;
};

type AnswerRequest = {
  question: string;
  answer: string;
};

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || `${response.status} ${response.statusText}`);
  }

  return response.json() as Promise<T>;
}

export const api = {
  baseUrl: API_BASE_URL,
  health: (portPath = '/actuator/health') => request<{ status: string }>(portPath),
  register: (email: string, password: string, role: Role) =>
    request<AuthResponse>('/api/auth/register', {
      method: 'POST',
      body: JSON.stringify({ email, password, role }),
    }),
  login: (email: string, password: string) =>
    request<AuthResponse>('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    }),
  createInterview: (candidateId: string, role: string) =>
    request<InterviewResponse>('/api/interviews', {
      method: 'POST',
      body: JSON.stringify({ candidateId, role }),
    }),
  submitInterview: (interviewId: string, answers: AnswerRequest[]) =>
    request<InterviewResponse>(`/api/interviews/${interviewId}/submit`, {
      method: 'POST',
      body: JSON.stringify({ answers }),
    }),
  getEvaluation: (interviewId: string) =>
    request<EvaluationResult>(`/api/evaluations/interviews/${interviewId}`),
  createCheckout: (customerId: string, planCode: string) =>
    request<CheckoutResponse>('/api/payments/checkout', {
      method: 'POST',
      body: JSON.stringify({ customerId, planCode }),
    }),
  completePayment: (checkoutSessionId: string) =>
    request<PaymentResponse>('/api/payments/callbacks/mock', {
      method: 'POST',
      headers: {
        'Idempotency-Key': `frontend-${crypto.randomUUID()}`,
      },
      body: JSON.stringify({ checkoutSessionId }),
    }),
};
