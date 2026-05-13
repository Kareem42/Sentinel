import React, {type FormEvent, useState} from "react";
import { useNavigate, Link } from "react-router-dom" ;
import api from '../api/axiosConfig';

export const Register: React.FC = () => {
    const [formData, setFormData] = useState({
        username: "",
        password: "",
        confirmPassword: "",
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        if (formData.password !== formData.confirmPassword) {
            return setError("Passwords don't match");
        }
        setLoading(true);
        setError('');

        try{
            await api.post('/registration/register', {
                username: formData.username,
                password: formData.password,
            });

            alert('Registration successful! Redirecting to login...');
            navigate('/');
        } catch (err: any) {
            setError(err.response?.data?.message || 'Registration failed. Try a different username.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <form onSubmit={handleSubmit} className="auth-form">
                <h2>Join Sentinel</h2>
                {error && <p className="error-message">{error}</p>}

                <input type="text"
                       placeholder="Username"
                       required
                       value={formData.username}
                       onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                       />
                <input type="password"
                       placeholder="Password"
                       required
                       value={formData.password}
                       onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                       />
                <input type="password"
                       placeholder="Confirm Password"
                       required
                       value={formData.confirmPassword}
                       onChange={(e) => setFormData({ ...formData, confirmPassword: e.target.value })}
                />
                <button type="submit" disabled={loading}>
                    {loading ? 'Creating Account...' : 'Register'}
                </button>
                <p>
                    Already have an account? <Link to="/login">Login here</Link>
                </p>
            </form>
        </div>
    )
}