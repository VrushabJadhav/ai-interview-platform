import {
  Activity,
  Bot,
  CheckCircle2,
  CreditCard,
  FileQuestion,
  Gauge,
  Loader2,
  LogIn,
  Play,
  RefreshCw,
  ShieldCheck,
  Sparkles,
  UserRound,
} from 'lucide-react';
import { useMemo, useState } from 'react';
import { api, AuthResponse, CheckoutResponse, EvaluationResult, InterviewResponse, PaymentResponse, Role } from './api';

type StepKey = 'auth' | 'interview' | 'evaluation' | 'payment';
type ServiceHealth = Record<string, 'UP' | 'DOWN' | 'CHECKING'>;

const defaultAnswers = [
  {
    question: 'Explain Kafka consumer groups.',
    answer:
      'Consumer groups let service instances share topic partitions so each event is processed by one member while the group scales horizontally.',
  },
  {
    question: 'How do you make payment callbacks idempotent?',
    answer:
      'Persist the idempotency key before completing the operation, then return the existing result when the same callback is retried.',
  },
  {
    question: 'When would you use Redis in this platform?',
    answer:
      'Redis is useful for gateway rate limiting, fast session metadata, and cached evaluation reads after results are persisted in PostgreSQL.',
  },
];

const services = [
  { name: 'Gateway', path: '/actuator/health' },
  { name: 'Auth', path: '/health/auth' },
  { name: 'Interview', path: '/health/interview' },
  { name: 'AI Evaluation', path: '/health/ai-evaluation' },
  { name: 'Payment', path: '/health/payment' },
  { name: 'Notification', path: '/health/notification' },
];

function makeDemoEmail() {
  return `candidate.${Date.now()}@demo.local`;
}

function scoreAverage(result: EvaluationResult | null) {
  if (!result) return 0;
  return Math.round((result.correctness + result.clarity + result.depth + result.communication) / 4);
}

export function App() {
  const [email, setEmail] = useState(makeDemoEmail());
  const [password, setPassword] = useState('Password123!');
  const [role, setRole] = useState<Role>('CANDIDATE');
  const [targetRole, setTargetRole] = useState('Java Backend Engineer');
  const [answers, setAnswers] = useState(defaultAnswers);
  const [planCode, setPlanCode] = useState('PRO');
  const [auth, setAuth] = useState<AuthResponse | null>(null);
  const [interview, setInterview] = useState<InterviewResponse | null>(null);
  const [evaluation, setEvaluation] = useState<EvaluationResult | null>(null);
  const [checkout, setCheckout] = useState<CheckoutResponse | null>(null);
  const [payment, setPayment] = useState<PaymentResponse | null>(null);
  const [health, setHealth] = useState<ServiceHealth>({});
  const [busyStep, setBusyStep] = useState<StepKey | 'demo' | 'health' | null>(null);
  const [error, setError] = useState<string | null>(null);

  const activeStep = useMemo(() => {
    if (payment?.status === 'COMPLETED') return 'payment';
    if (evaluation) return 'evaluation';
    if (interview?.status === 'SUBMITTED') return 'evaluation';
    if (auth) return 'interview';
    return 'auth';
  }, [auth, evaluation, interview, payment]);

  const average = scoreAverage(evaluation);

  async function run<T>(step: typeof busyStep, work: () => Promise<T>) {
    setBusyStep(step);
    setError(null);
    try {
      return await work();
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Unexpected error';
      setError(message);
      throw err;
    } finally {
      setBusyStep(null);
    }
  }

  async function checkHealth() {
    await run('health', async () => {
      setHealth(Object.fromEntries(services.map((service) => [service.name, 'CHECKING'])));
      const results: ServiceHealth = {};
      await Promise.all(
        services.map(async (service) => {
          try {
            const response = await api.health(service.path);
            results[service.name] = response.status === 'UP' ? 'UP' : 'DOWN';
          } catch {
            results[service.name] = 'DOWN';
          }
        }),
      );
      setHealth(results);
    });
  }

  async function registerCandidate() {
    await run('auth', async () => {
      const response = await api.register(email, password, role);
      setAuth(response);
      setInterview(null);
      setEvaluation(null);
      setCheckout(null);
      setPayment(null);
    });
  }

  async function startInterview() {
    if (!auth) return;
    await run('interview', async () => {
      const response = await api.createInterview(auth.userId, targetRole);
      setInterview(response);
      setEvaluation(null);
    });
  }

  async function submitInterview() {
    if (!interview) return;
    await run('interview', async () => {
      const submitted = await api.submitInterview(interview.id, answers);
      setInterview(submitted);
      await pollEvaluation(submitted.id);
    });
  }

  async function pollEvaluation(interviewId: string) {
    for (let attempt = 0; attempt < 10; attempt += 1) {
      try {
        const result = await api.getEvaluation(interviewId);
        setEvaluation(result);
        return result;
      } catch {
        await new Promise((resolve) => window.setTimeout(resolve, 1400));
      }
    }
    throw new Error('Evaluation is still processing. Try refresh evaluation in a moment.');
  }

  async function refreshEvaluation() {
    if (!interview) return;
    await run('evaluation', async () => pollEvaluation(interview.id));
  }

  async function createCheckout() {
    if (!auth) return;
    await run('payment', async () => {
      const response = await api.createCheckout(auth.userId, planCode);
      setCheckout(response);
      setPayment(null);
    });
  }

  async function completePayment() {
    if (!checkout) return;
    await run('payment', async () => {
      const response = await api.completePayment(checkout.checkoutSessionId);
      setPayment(response);
    });
  }

  async function runDemo() {
    await run('demo', async () => {
      const demoEmail = makeDemoEmail();
      setEmail(demoEmail);
      const registered = await api.register(demoEmail, password, role);
      setAuth(registered);
      const created = await api.createInterview(registered.userId, targetRole);
      setInterview(created);
      const submitted = await api.submitInterview(created.id, answers);
      setInterview(submitted);
      const result = await pollEvaluation(submitted.id);
      setEvaluation(result);
      const checkoutSession = await api.createCheckout(registered.userId, planCode);
      setCheckout(checkoutSession);
      const completed = await api.completePayment(checkoutSession.checkoutSessionId);
      setPayment(completed);
    });
  }

  function updateAnswer(index: number, field: 'question' | 'answer', value: string) {
    setAnswers((current) => current.map((answer, i) => (i === index ? { ...answer, [field]: value } : answer)));
  }

  return (
    <main className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <div className="brand-mark">
            <Bot size={24} />
          </div>
          <div>
            <h1>AI Interview Platform</h1>
            <p>Microservices demo console</p>
          </div>
        </div>

        <nav className="step-list" aria-label="Workflow">
          <StepItem icon={<ShieldCheck />} label="Auth" active={activeStep === 'auth'} complete={Boolean(auth)} />
          <StepItem
            icon={<FileQuestion />}
            label="Interview"
            active={activeStep === 'interview'}
            complete={interview?.status === 'SUBMITTED'}
          />
          <StepItem icon={<Sparkles />} label="Evaluation" active={activeStep === 'evaluation'} complete={Boolean(evaluation)} />
          <StepItem icon={<CreditCard />} label="Payment" active={activeStep === 'payment'} complete={payment?.status === 'COMPLETED'} />
        </nav>

        <button className="primary full" onClick={runDemo} disabled={busyStep !== null}>
          {busyStep === 'demo' ? <Loader2 className="spin" size={18} /> : <Play size={18} />}
          Run Full Demo
        </button>

        <section className="health-panel">
          <div className="panel-heading">
            <span>Service Health</span>
            <button className="icon-button" onClick={checkHealth} aria-label="Refresh service health">
              {busyStep === 'health' ? <Loader2 className="spin" size={16} /> : <RefreshCw size={16} />}
            </button>
          </div>
          <div className="health-grid">
            {services.map((service) => (
              <div className="health-row" key={service.name}>
                <span>{service.name}</span>
                <StatusPill status={health[service.name] ?? 'UP'} />
              </div>
            ))}
          </div>
        </section>
      </aside>

      <section className="workspace">
        <header className="topbar">
          <div>
            <p className="eyebrow">Gateway</p>
            <h2>{api.baseUrl}</h2>
          </div>
          <div className="run-state">
            <Activity size={18} />
            <span>{busyStep ? 'Working' : 'Ready'}</span>
          </div>
        </header>

        {error && <div className="error-banner">{error}</div>}

        <section className="summary-strip">
          <Metric icon={<UserRound />} label="Candidate" value={auth?.email ?? 'Not registered'} />
          <Metric icon={<FileQuestion />} label="Interview" value={interview?.status ?? 'Not started'} />
          <Metric icon={<Gauge />} label="AI score" value={evaluation ? `${average}/100` : 'Pending'} />
          <Metric icon={<CreditCard />} label="Payment" value={payment?.status ?? checkout?.checkoutSessionId ?? 'Not started'} />
        </section>

        <section className="main-grid">
          <div className="column">
            <Panel title="Candidate Access" icon={<LogIn size={18} />}>
              <div className="form-grid">
                <label>
                  Email
                  <input value={email} onChange={(event) => setEmail(event.target.value)} />
                </label>
                <label>
                  Password
                  <input type="password" value={password} onChange={(event) => setPassword(event.target.value)} />
                </label>
                <label>
                  Role
                  <select value={role} onChange={(event) => setRole(event.target.value as Role)}>
                    <option value="CANDIDATE">Candidate</option>
                    <option value="RECRUITER">Recruiter</option>
                    <option value="ADMIN">Admin</option>
                  </select>
                </label>
              </div>
              <button className="primary" onClick={registerCandidate} disabled={busyStep !== null}>
                {busyStep === 'auth' ? <Loader2 className="spin" size={18} /> : <ShieldCheck size={18} />}
                Register
              </button>
              {auth && <pre className="result-box">{JSON.stringify({ userId: auth.userId, role: auth.role }, null, 2)}</pre>}
            </Panel>

            <Panel title="Interview Session" icon={<FileQuestion size={18} />}>
              <label>
                Target role
                <input value={targetRole} onChange={(event) => setTargetRole(event.target.value)} />
              </label>
              <button className="secondary" onClick={startInterview} disabled={!auth || busyStep !== null}>
                Create Session
              </button>
              <div className="answer-list">
                {answers.map((answer, index) => (
                  <div className="answer-editor" key={index}>
                    <input
                      aria-label={`Question ${index + 1}`}
                      value={answer.question}
                      onChange={(event) => updateAnswer(index, 'question', event.target.value)}
                    />
                    <textarea
                      aria-label={`Answer ${index + 1}`}
                      value={answer.answer}
                      onChange={(event) => updateAnswer(index, 'answer', event.target.value)}
                    />
                  </div>
                ))}
              </div>
              <button className="primary" onClick={submitInterview} disabled={!interview || busyStep !== null}>
                {busyStep === 'interview' ? <Loader2 className="spin" size={18} /> : <Sparkles size={18} />}
                Submit for AI Evaluation
              </button>
            </Panel>
          </div>

          <div className="column">
            <Panel title="AI Evaluation" icon={<Sparkles size={18} />}>
              {evaluation ? (
                <>
                  <div className="score-hero">
                    <div>
                      <span>Overall</span>
                      <strong>{average}</strong>
                    </div>
                    <p>{evaluation.summary}</p>
                  </div>
                  <ScoreBar label="Correctness" value={evaluation.correctness} />
                  <ScoreBar label="Clarity" value={evaluation.clarity} />
                  <ScoreBar label="Depth" value={evaluation.depth} />
                  <ScoreBar label="Communication" value={evaluation.communication} />
                </>
              ) : (
                <div className="empty-state">
                  <Bot size={34} />
                  <span>Submit an interview to generate the mock AI evaluation.</span>
                </div>
              )}
              <button className="secondary" onClick={refreshEvaluation} disabled={!interview || busyStep !== null}>
                Refresh Evaluation
              </button>
            </Panel>

            <Panel title="Subscription Checkout" icon={<CreditCard size={18} />}>
              <div className="plan-row">
                {['FREE', 'PRO', 'TEAM'].map((plan) => (
                  <button
                    className={planCode === plan ? 'plan selected' : 'plan'}
                    key={plan}
                    onClick={() => setPlanCode(plan)}
                  >
                    {plan}
                  </button>
                ))}
              </div>
              <button className="secondary" onClick={createCheckout} disabled={!auth || busyStep !== null}>
                Create Checkout
              </button>
              {checkout && <pre className="result-box">{JSON.stringify(checkout, null, 2)}</pre>}
              <button className="primary" onClick={completePayment} disabled={!checkout || busyStep !== null}>
                {busyStep === 'payment' ? <Loader2 className="spin" size={18} /> : <CheckCircle2 size={18} />}
                Complete Mock Payment
              </button>
              {payment && (
                <div className="payment-success">
                  <CheckCircle2 size={20} />
                  <span>
                    {payment.status} · {payment.amount} {payment.currency}
                  </span>
                </div>
              )}
            </Panel>
          </div>
        </section>
      </section>
    </main>
  );
}

function StepItem({
  icon,
  label,
  active,
  complete,
}: {
  icon: React.ReactNode;
  label: string;
  active: boolean;
  complete: boolean;
}) {
  return (
    <div className={active ? 'step active' : complete ? 'step complete' : 'step'}>
      <span className="step-icon">{complete ? <CheckCircle2 size={18} /> : icon}</span>
      <span>{label}</span>
    </div>
  );
}

function StatusPill({ status }: { status: 'UP' | 'DOWN' | 'CHECKING' }) {
  return <span className={`status ${status.toLowerCase()}`}>{status}</span>;
}

function Metric({ icon, label, value }: { icon: React.ReactNode; label: string; value: string }) {
  return (
    <div className="metric">
      <span className="metric-icon">{icon}</span>
      <div>
        <span>{label}</span>
        <strong>{value}</strong>
      </div>
    </div>
  );
}

function Panel({ title, icon, children }: { title: string; icon: React.ReactNode; children: React.ReactNode }) {
  return (
    <article className="panel">
      <div className="panel-heading">
        <div className="panel-title">
          {icon}
          <h3>{title}</h3>
        </div>
      </div>
      {children}
    </article>
  );
}

function ScoreBar({ label, value }: { label: string; value: number }) {
  return (
    <div className="score-row">
      <div>
        <span>{label}</span>
        <strong>{value}</strong>
      </div>
      <div className="score-track">
        <span style={{ width: `${value}%` }} />
      </div>
    </div>
  );
}
