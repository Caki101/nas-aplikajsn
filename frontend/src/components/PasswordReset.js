import React, { useState } from 'react';

const PasswordReset = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [verificationMessage, setVerificationMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('http://26.10.184.197:8080/auth/check_identity', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        setVerificationMessage('Verification sent to email.')
      } else {
        setVerificationMessage('Wrong password')
      }
    } catch (error) {
      setVerificationMessage('Error')
    }
  };

  return (
    <div className="password-reset-page">
      <div className="password-reset-container">
        <div className="password-reset-header">
          <h2>Reset Password</h2>
          <div className="underline"></div>
        </div>

        <div className="password-reset-inputs">
          <div className="password-reset-input">
            <i className="fa fa-envelope"></i>
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="password-reset-input">
            <i className="fa fa-lock"></i>
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          {verificationMessage && (
            <div className='verification-message'>{verificationMessage}</div>
          )}
        </div>

        <div className="password-reset-submit-container">
          <button type="submit" className="password-reset-submit" onClick={handleSubmit}>Reset Password</button>
        </div>
      </div>
    </div>
  );
};

export default PasswordReset;