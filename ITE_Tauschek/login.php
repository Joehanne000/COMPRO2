<?php
session_start();
$message = "";
$file = "users.csv";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $email = trim($_POST["email"]);
    $password = $_POST["password"];

    if (file_exists($file)) {
        $handle = fopen($file, "r");
        while (($data = fgetcsv($handle)) !== FALSE) {
            if ($data[1] == $email) {
                if (password_verify($password, $data[2])) {
                    $_SESSION["username"] = $data[0];
                    header("Location: dashboard.php");
                    exit();
                } else {
                    $message = "Invalid password!";
                    break;
                }
            }
        }
        fclose($handle);
    }

    if ($message == "") {
        $message = "User not found!";
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - NearBuy</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        html, body {
            width: 100%;
            height: 100%;
            overflow: hidden;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
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
            background: radial-gradient(circle at 20% 50%, rgba(74, 144, 226, 0.1) 0%, transparent 50%),
                        radial-gradient(circle at 80% 80%, rgba(209, 84, 255, 0.1) 0%, transparent 50%);
            animation: float 20s ease-in-out infinite;
            pointer-events: none;
        }

        @keyframes float {
            0%, 100% { transform: translate(0, 0); }
            50% { transform: translate(30px, -30px); }
        }

        .login-container {
            position: relative;
            z-index: 1;
            width: 100%;
            max-width: 420px;
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

        .login-box {
            background: rgba(255, 255, 255, 0.08);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.15);
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
        }

        .login-header {
            text-align: center;
            margin-bottom: 35px;
        }

        .login-header h1 {
            color: #fff;
            font-size: 2.2em;
            font-weight: 700;
            letter-spacing: -0.5px;
            margin-bottom: 8px;
        }

        .login-header p {
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

        input[type="email"]:focus,
        input[type="password"]:focus {
            background: rgba(255, 255, 255, 0.1);
            border-color: #4a90e2;
            box-shadow: 0 0 20px rgba(74, 144, 226, 0.3);
            transform: translateY(-2px);
        }

        input::placeholder {
            color: rgba(255, 255, 255, 0.4);
        }

        .error-message {
            color: #ff6b6b;
            font-size: 0.85em;
            margin-top: 8px;
            display: flex;
            align-items: center;
            gap: 6px;
            animation: shake 0.4s ease-out;
            padding: 12px 16px;
            background: rgba(255, 107, 107, 0.1);
            border-left: 3px solid #ff6b6b;
            border-radius: 8px;
        }

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-5px); }
            75% { transform: translateX(5px); }
        }

        .error-message::before {
            content: '⚠';
            font-weight: bold;
            font-size: 1.1em;
        }

        .submit-btn {
            width: 100%;
            padding: 13px;
            background: linear-gradient(135deg, #4a90e2, #357abd);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 1em;
            font-weight: 600;
            cursor: pointer;
            margin-top: 25px;
            transition: all 0.3s ease;
            letter-spacing: 0.5px;
            box-shadow: 0 4px 15px rgba(74, 144, 226, 0.3);
        }

        .submit-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(74, 144, 226, 0.4);
        }

        .submit-btn:active {
            transform: translateY(0);
        }

        .form-footer {
            text-align: center;
            margin-top: 25px;
            animation: slideInLeft 0.6s ease-out backwards 0.4s;
        }

        .form-footer p {
            color: rgba(255, 255, 255, 0.6);
            font-size: 0.9em;
        }

        .form-footer a {
            color: #4a90e2;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease;
            margin-left: 4px;
        }

        .form-footer a:hover {
            color: #6ba3ff;
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
            background: #4a90e2;
            top: -100px;
            left: -100px;
            animation: rotate 20s linear infinite;
        }

        .deco2 {
            width: 200px;
            height: 200px;
            background: #d154ff;
            bottom: -50px;
            right: -50px;
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

    <div class="login-container">
        <div class="login-box">
            <div class="login-header">
                <h1>NearBuy</h1>
                <p>Welcome Back</p>
            </div>

            <form method="POST">
                <div class="form-group">
                    <label for="email">Email Address</label>
                    <input type="email" id="email" name="email" placeholder="you@example.com" required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" placeholder="••••••••" required>
                </div>

                <?php if ($message): ?>
                    <div class="error-message"><?php echo htmlspecialchars($message); ?></div>
                <?php endif; ?>

                <button type="submit" class="submit-btn">Sign In</button>
            </form>

            <div class="form-footer">
                <p>Don't have an account? <a href="register.php">Create one</a></p>
            </div>
        </div>
    </div>
</body>
</html>