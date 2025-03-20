import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../App.css";

const SignUp = () => {
    const [isSignUp, setIsSignUp] = useState(true);
    const [isForgotPassword, setIsForgotPassword] = useState(false);
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (isForgotPassword) {
            handleForgotPassword();
            return;
        }
        
        const userData = { username, email, password };
        const apiUrl = isSignUp ? 'http://26.10.184.197:8080/api/userSignUp' : 'http://26.10.184.197:8080/api/userLogin';
    
        try {
            const response = await fetch(apiUrl, {
                method: 'POST',
                headers: {
                    "Api-Key-Header": "api_123",
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData)
            });
    
            console.log("Response Status:", response.status);
            const textResponse = await response.text();
            console.log("Response Text:", textResponse);

            if (!response.ok) {
                try {
                    const result = JSON.parse(textResponse);
                    console.log("Error details:", result);
    
                    setErrorMessage(result.message);
                    setSuccessMessage('');
                } catch (error) {
                    console.error("Failed to parse JSON response:", error);
                    setErrorMessage(textResponse);
                    setSuccessMessage('');
                }
                return;
            }
    
            const result = textResponse ? JSON.parse(textResponse) : {};
            console.log("Success result:", result);
            setErrorMessage('');
            setSuccessMessage(result.message);

            const userInfo = result.username || username;
            const userEmail = result.email || email
            localStorage.setItem('authToken', result.jwtToken);
            localStorage.setItem('username', userInfo);
            localStorage.setItem('email', userEmail)
            window.dispatchEvent(new Event("storage"));
            navigate('/Profile');

            setUsername(userInfo);
            setEmail('');
            setPassword('');
    
        } catch (error) {
            console.error("Request Failed:", error);
            setErrorMessage('Request Failed: Please try again');
            setSuccessMessage('');
        }
    };

    const handleForgotPassword = async () => {
        if (!email) {
            setErrorMessage('Please enter your email address.');
            return;
        }

        try {
            const response = await fetch('http://26.10.184.197:8080/api/forgotPassword', {
                method: 'POST',
                headers: {
                    "Api-Key-Header": "api_123",
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email })
            });

            console.log("Forgot Password Response Status:", response.status);
            const textResponse = await response.text();
            console.log("Forgot Password Response Text:", textResponse);

            if (!response.ok) {
                setErrorMessage(textResponse);
                setSuccessMessage('');
                return;
            }

            setSuccessMessage("Password reset link sent to your email!");
            setErrorMessage('');
            setIsForgotPassword(false);

        } catch (error) {
            console.error("Forgot Password Request Failed:", error);
            setErrorMessage('Failed to send reset email. Please try again.');
            setSuccessMessage('');
        }
    };

    return (
        <div className="sign-up">
            <video src="/videos/video-9.mp4" autoPlay loop onLoadedMetadata={(e) => e.target.volume = 0.1} muted />
            <div className="sign-up-container">
                <div className="sign-up-header">
                    <div className="text">
                        {isForgotPassword ? "Forgot Password" : isSignUp ? "Sign Up" : "Login"}
                    </div>
                    <div className="underline"></div>
                </div>
                <div className="sign-up-inputs">
                    {isForgotPassword ? (
                        <>
                            <p>Enter the email</p>
                            <div className="sign-up-input">
                                <i className="fa fa-envelope"></i>
                                <input
                                    type="email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    placeholder="Email"
                                    required
                                />
                            </div>
                        </>
                    ) : (
                        <>
                            {isSignUp && (
                                <div className="sign-up-input">
                                    <i className="fa fa-user"></i>
                                    <input
                                        type="text"
                                        value={username}
                                        onChange={(e) => setUsername(e.target.value)}
                                        placeholder="Username"
                                        required
                                    />
                                </div>
                            )}
                            <div className="sign-up-input">
                                <i className="fa fa-envelope"></i>
                                <input
                                    type="email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    placeholder="Email"
                                    required
                                />
                            </div>
                            <div className="sign-up-input">
                                <i className="fa fa-lock"></i>
                                <input
                                    type="password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    placeholder="Password"
                                />
                            </div>
                        </>
                    )}
                </div>

                {errorMessage && <p style={{ color: '#242424', textAlign: 'center', paddingTop: '10px' }}>{errorMessage}</p>}
                {successMessage && <p style={{ color: '#242424', textAlign: 'center', paddingTop: '10px' }}>{successMessage}</p>}

                <div className="sign-up-footer">
                    {!isForgotPassword && !isSignUp && (
                        <div 
                            className="sign-up-forgot-password"
                            onClick={() => {
                                setIsForgotPassword(true);
                                setErrorMessage('');
                                setSuccessMessage('');
                            }}
                        >
                            Lost Password?
                        </div>
                    )}
                    {!isForgotPassword && (
                        <div
                            className="sign-up-already-account"
                            onClick={() => {
                                setIsSignUp(!isSignUp);
                                setUsername('');
                                setEmail('');
                                setPassword('');
                                setErrorMessage('');
                                setSuccessMessage('');
                            }}
                        >
                            {isSignUp ? "Already have an account?" : "Don't have an account? Make one!"}
                        </div>
                    )}
                    {isForgotPassword && (
                        <div
                            className="sign-up-already-account"
                            onClick={() => {
                                setIsForgotPassword(false);
                                setErrorMessage('');
                                setSuccessMessage('');
                            }}
                        >
                            Back to Login
                        </div>
                    )}
                </div>

                <div className="sign-up-submit-container">
                    <button type="submit" className="sign-up-submit" onClick={handleSubmit}>
                        {isForgotPassword ? "Reset Password" : isSignUp ? "Sign Up" : "Login"}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default SignUp;
