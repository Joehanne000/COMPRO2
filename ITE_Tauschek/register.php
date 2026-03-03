<?php
$message = "";
$file = "users.csv";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $username = trim($_POST["username"]);
    $email = trim($_POST["email"]);
    $password = $_POST["password"];
    $confirm_password = $_POST["confirm_password"];

    if (empty($username) || empty($email) || empty($password)) {
        $message = "All fields are required!";
    } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $message = "Invalid email!";
    } elseif ($password !== $confirm_password) {
        $message = "Passwords do not match!";
    } else {
        if (file_exists($file)) {
            $handle = fopen($file, "r");
            while (($data = fgetcsv($handle)) !== FALSE) {
                if ($data[1] == $email || $data[0] == $username) {
                    $message = "Username or Email already exists!";
                    break;
                }
            }
            fclose($handle);
        }

        if ($message == "") {
            $hashed_password = password_hash($password, PASSWORD_DEFAULT);
            $handle = fopen($file, "a");
            fputcsv($handle, [$username, $email, $hashed_password]);
            fclose($handle);
            $message = "Registration successful! You can now login.";
        }
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - NearBuy</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        html, body {
            width: 100%;
            height: 100%;
            overflow-x: hidden;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #1a0033, #33001a, #1a3300);
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            position: relative;
        }

        body::before {
            content: '';
            position: fixed;
            top: -50%;
            right: -50%;
            width: 200%;
            height: 200%;
            background: radial-gradient(circle at 20% 50%, rgba(209, 84, 255, 0.1) 0%, transparent 50%),
                        radial-gradient(circle at 80% 80%, rgba(100, 200, 255, 0.1) 0%, transparent 50%);
            animation: float 25s ease-in-out infinite;
            pointer-events: none;
        }

        @keyframes float {
            0%, 100% { transform: translate(0, 0); }
            50% { transform: translate(-30px, 30px); }
        }

        .register-container {
            position: relative;
            z-index: 1;
            width: 100%;
            max-width: 480px;
            padding: 20px;
            animation: slideUp 0.8s ease-out;
        }

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .register-box {
            background: rgba(255, 255, 255, 0.08);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.15);
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
        }

        .register-header {
            text-align: center;
            margin-bottom: 35px;
        }

        .register-header h1 {
            color: #fff;
            font-size: 2.2em;
            font-weight: 700;
            letter-spacing: -0.5px;
            margin-bottom: 8px;
        }

        .register-header p {
            color: rgba(255, 255, 255, 0.6);
            font-size: 0.95em;
            letter-spacing: 0.3px;
        }

        .form-group {
            margin-bottom: 20px;
            animation: slideInLeft 0.6s ease-out backwards;
        }

        .form-group:nth-child(1) { animation-delay: 0.2s; }
        .form-group:nth-child(2) { animation-delay: 0.3s; }
        .form-group:nth-child(3) { animation-delay: 0.4s; }
        .form-group:nth-child(4) { animation-delay: 0.5s; }

        @keyframes slideInLeft {
            from {
                opacity: 0;
                transform: translateX(-20px);
            }
            to {
                opacity: 1;
                transform: translateX(0);
            }
        }

        label {
            display: block;
            color: rgba(255, 255, 255, 0.8);
            font-size: 0.9em;
            font-weight: 600;
            margin-bottom: 8px;
            letter-spacing: 0.3px;
        }

        input[type="text"],
        input[type="email"],
        input[type="password"] {
            width: 100%;
            padding: 12px 16px;
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 10px;
            color: #fff;
            font-size: 0.95em;
            transition: all 0.3s ease;
            outline: none;
        }

        input[type="text"]:focus,
        input[type="email"]:focus,
        input[type="password"]:focus {
            background: rgba(255, 255, 255, 0.1);
            border-color: #d154ff;
            box-shadow: 0 0 20px rgba(209, 84, 255, 0.3);
            transform: translateY(-2px);
        }

        input::placeholder {
            color: rgba(255, 255, 255, 0.4);
        }

        .password-strength {
            display: none;
            margin-top: 8px;
            font-size: 0.8em;
            padding: 8px 12px;
            border-radius: 8px;
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.1);
        }

        .password-strength.show {
            display: block;
            animation: slideUp 0.3s ease-out;
        }

        .strength-meter {
            height: 4px;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 2px;
            overflow: hidden;
            margin-bottom: 6px;
        }

        .strength-bar {
            height: 100%;
            width: 0%;
            transition: all 0.3s ease;
            border-radius: 2px;
        }

        .strength-text {
            color: rgba(255, 255, 255, 0.6);
        }

        .error-message {
            color: #ff6b6b;
            font-size: 0.85em;
            margin-top: 15px;
            padding: 12px 16px;
            background: rgba(255, 107, 107, 0.1);
            border-left: 3px solid #ff6b6b;
            border-radius: 8px;
            display: flex;
            align-items: center;
            gap: 8px;
            animation: shake 0.4s ease-out;
        }

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-5px); }
            75% { transform: translateX(5px); }
        }

        .error-message::before {
            content: '⚠';
            font-weight: bold;
            font-size: 1.2em;
        }

        .success-message {
            color: #51cf66;
            font-size: 0.9em;
            margin-top: 15px;
            padding: 12px 16px;
            background: rgba(81, 207, 102, 0.1);
            border-left: 3px solid #51cf66;
            border-radius: 8px;
            display: flex;
            align-items: center;
            gap: 8px;
            animation: slideInLeft 0.4s ease-out;
        }

        .success-message::before {
            content: '✓';
            font-weight: bold;
            font-size: 1.2em;
        }

        .submit-btn {
            width: 100%;
            padding: 13px;
            background: linear-gradient(135deg, #d154ff, #a435cc);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 1em;
            font-weight: 600;
            cursor: pointer;
            margin-top: 25px;
            transition: all 0.3s ease;
            letter-spacing: 0.5px;
            box-shadow: 0 4px 15px rgba(209, 84, 255, 0.3);
        }

        .submit-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(209, 84, 255, 0.4);
        }

        .submit-btn:active {
            transform: translateY(0);
        }

        .form-footer {
            text-align: center;
            margin-top: 25px;
            animation: slideInLeft 0.6s ease-out backwards 0.6s;
        }

        .form-footer p {
            color: rgba(255, 255, 255, 0.6);
            font-size: 0.9em;
        }

        .form-footer a {
            color: #d154ff;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease;
            margin-left: 4px;
        }

        .form-footer a:hover {
            color: #ff6bff;
            text-decoration: underline;
        }

        .decoration {
            position: fixed;
            border-radius: 50%;
            pointer-events: none;
            opacity: 0.1;
        }

        .deco1 {
            width: 300px;
            height: 300px;
            background: #d154ff;
            top: -100px;
            right: -100px;
            animation: rotate 20s linear infinite;
        }

        .deco2 {
            width: 200px;
            height: 200px;
            background: #64c8ff;
            bottom: -50px;
            left: -50px;
            animation: rotate 25s linear infinite reverse;
        }

        @keyframes rotate {
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
        }
    </style>
</head>
<body>
    <div class="decoration deco1"></div>
    <div class="decoration deco2"></div>

    <div class="register-container">
        <div class="register-box">
            <div class="register-header">
                <h1>NearBuy</h1>
                <p>Create Your Account</p>
            </div>

            <form name="regForm" method="POST">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" placeholder="Choose a username" required>
                </div>

                <div class="form-group">
                    <label for="email">Email Address</label>
                    <input type="email" id="email" name="email" placeholder="you@example.com" required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" placeholder="••••••••" required onkeyup="checkPasswordStrength()">
                    <div class="password-strength" id="strengthIndicator">
                        <div class="strength-meter">
                            <div class="strength-bar" id="strengthBar"></div>
                        </div>
                        <div class="strength-text" id="strengthText">Weak password</div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="confirm_password">Confirm Password</label>
                    <input type="password" id="confirm_password" name="confirm_password" placeholder="••••••••" required onkeyup="checkPasswordMatch()">
                </div>

                <?php if ($message): ?>
                    <?php if (strpos($message, 'successful') !== false): ?>
                        <div class="success-message"><?php echo htmlspecialchars($message); ?></div>
                    <?php else: ?>
                        <div class="error-message"><?php echo htmlspecialchars($message); ?></div>
                    <?php endif; ?>
                <?php endif; ?>

                <button type="submit" class="submit-btn" id="submitBtn">Create Account</button>
            </form>

            <div class="form-footer">
                <p>Already have an account? <a href="login.php">Sign in</a></p>
            </div>
        </div>
    </div>

    <script>
        function checkPasswordStrength() {
            const password = document.getElementById('password').value;
            const indicator = document.getElementById('strengthIndicator');
            const bar = document.getElementById('strengthBar');
            const text = document.getElementById('strengthText');

            indicator.classList.add('show');

            let strength = 0;
            if (password.length >= 6) strength += 20;
            if (password.length >= 8) strength += 20;
            if (/[A-Z]/.test(password)) strength += 20;
            if (/[0-9]/.test(password)) strength += 20;
            if (/[^A-Za-z0-9]/.test(password)) strength += 20;

            bar.style.width = strength + '%';

            if (strength < 40) {
                bar.style.background = '#ff6b6b';
                text.textContent = 'Weak password';
            } else if (strength < 70) {
                bar.style.background = '#ffd43b';
                text.textContent = 'Fair password';
            } else {
                bar.style.background = '#51cf66';
                text.textContent = 'Strong password';
            }

            checkPasswordMatch();
        }

        function checkPasswordMatch() {
            const password = document.getElementById('password').value;
            const confirm = document.getElementById('confirm_password').value;
            const confirmInput = document.getElementById('confirm_password');

            if (confirm === '') return;

            if (password === confirm) {
                confirmInput.style.borderColor = '#51cf66';
                confirmInput.style.boxShadow = '0 0 15px rgba(81, 207, 102, 0.2)';
            } else {
                confirmInput.style.borderColor = '#ff6b6b';
                confirmInput.style.boxShadow = '0 0 15px rgba(255, 107, 107, 0.2)';
            }
        }

        document.getElementById('regForm').addEventListener('submit', function(e) {
            const password = document.getElementById('password').value;
            const confirm = document.getElementById('confirm_password').value;

            if (password.length < 6) {
                e.preventDefault();
                alert('Password must be at least 6 characters!');
                return false;
            }

            if (password !== confirm) {
                e.preventDefault();
                alert('Passwords do not match!');
                return false;
            }

            return true;
        });
    </script>
</body>
</html>