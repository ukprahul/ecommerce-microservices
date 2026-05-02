import { Form, Input, Button, Card, Typography, Alert, message } from 'antd'
import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { signup as apiSignup } from '../api/userApi'

const { Title } = Typography

export default function Signup() {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const navigate = useNavigate()

  const handleSubmit = async ({ email, password }) => {
    setLoading(true)
    setError(null)
    try {
      await apiSignup(email, password)
      message.success('Account created! Please log in.')
      navigate('/login')
    } catch (err) {
      setError(err.response?.data?.message || 'Signup failed. Try a different email.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '80vh' }}>
      <Card style={{ width: 400 }}>
        <Title level={3} style={{ textAlign: 'center', marginBottom: 24 }}>Create Account</Title>
        {error && <Alert type="error" message={error} style={{ marginBottom: 16 }} />}
        <Form layout="vertical" onFinish={handleSubmit}>
          <Form.Item name="email" label="Email" rules={[{ required: true, type: 'email' }]}>
            <Input size="large" placeholder="you@example.com" />
          </Form.Item>
          <Form.Item name="password" label="Password" rules={[{ required: true, min: 6 }]}>
            <Input.Password size="large" placeholder="Min 6 characters" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading} block size="large">
              Sign Up
            </Button>
          </Form.Item>
          <div style={{ textAlign: 'center' }}>
            Already have an account? <Link to="/login">Login</Link>
          </div>
        </Form>
      </Card>
    </div>
  )
}
