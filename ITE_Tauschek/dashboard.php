<?php
session_start();

if (!isset($_SESSION["username"])) {
    header("Location: login.php");
    exit();
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - NearBuy</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        html, body {
            width: 100%;
            height: 100%;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #0a0e27, #1a1a3e, #0d1b2a);
            min-height: 100vh;
            position: relative;
            overflow-x: hidden;
        }

        body::before {
            content: '';
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: radial-gradient(circle at 20% 50%, rgba(74, 144, 226, 0.08) 0%, transparent 50%),
                        radial-gradient(circle at 80% 80%, rgba(209, 84, 255, 0.08) 0%, transparent 50%);
            pointer-events: none;
            animation: bgPulse 8s ease-in-out infinite;
        }

        @keyframes bgPulse {
            0%, 100% { opacity: 1; }
            50% { opacity: 0.8; }
        }

        .dashboard {
            position: relative;
            z-index: 1;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        nav {
            background: rgba(15, 15, 45, 0.7);
            backdrop-filter: blur(10px);
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
            padding: 20px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            animation: slideDown 0.6s ease-out;
        }

        @keyframes slideDown {
            from {
                opacity: 0;
                transform: translateY(-20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .nav-brand {
            font-size: 1.8em;
            font-weight: 700;
            color: #fff;
            letter-spacing: -0.5px;
        }

        .nav-brand span {
            background: linear-gradient(135deg, #4a90e2, #d154ff);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .nav-links {
            display: flex;
            align-items: center;
            gap: 30px;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .user-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: linear-gradient(135deg, #4a90e2, #d154ff);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: bold;
            font-size: 1.2em;
        }

        .username-display {
            color: rgba(255, 255, 255, 0.9);
            font-weight: 600;
        }

        .logout-btn {
            padding: 10px 24px;
            background: linear-gradient(135deg, #4a90e2, #357abd);
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            letter-spacing: 0.3px;
            box-shadow: 0 4px 15px rgba(74, 144, 226, 0.2);
        }

        .logout-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(74, 144, 226, 0.3);
        }

        .logout-btn:active {
            transform: translateY(0);
        }

        .main-content {
            flex: 1;
            padding: 60px 40px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .welcome-container {
            text-align: center;
            max-width: 600px;
            animation: fadeInScale 0.8s ease-out;
        }

        @keyframes fadeInScale {
            from {
                opacity: 0;
                transform: scale(0.95);
            }
            to {
                opacity: 1;
                transform: scale(1);
            }
        }

        .welcome-icon {
            font-size: 4em;
            margin-bottom: 20px;
            animation: bounce 2s ease-in-out infinite;
        }

        @keyframes bounce {
            0%, 100% { transform: translateY(0); }
            50% { transform: translateY(-20px); }
        }

        .welcome-title {
            color: #fff;
            font-size: 2.5em;
            margin-bottom: 15px;
            letter-spacing: -0.5px;
            line-height: 1.2;
        }

        .welcome-title .highlight {
            background: linear-gradient(135deg, #4a90e2, #d154ff);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .welcome-text {
            color: rgba(255, 255, 255, 0.7);
            font-size: 1.1em;
            margin-bottom: 30px;
            line-height: 1.6;
            letter-spacing: 0.3px;
        }

        .card-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-top: 40px;
        }

        .dashboard-card {
            background: rgba(255, 255, 255, 0.08);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.15);
            border-radius: 15px;
            padding: 25px;
            transition: all 0.3s ease;
            cursor: pointer;
            animation: slideUp 0.6s ease-out backwards;
        }

        .dashboard-card:nth-child(1) { animation-delay: 0.2s; }
        .dashboard-card:nth-child(2) { animation-delay: 0.3s; }
        .dashboard-card:nth-child(3) { animation-delay: 0.4s; }

        .dashboard-card:hover {
            transform: translateY(-10px);
            background: rgba(255, 255, 255, 0.12);
            border-color: rgba(255, 255, 255, 0.25);
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
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

        .card-icon {
            font-size: 2.5em;
            margin-bottom: 15px;
        }

        .card-title {
            color: #fff;
            font-size: 1.2em;
            font-weight: 600;
            margin-bottom: 8px;
            letter-spacing: 0.2px;
        }

        .card-text {
            color: rgba(255, 255, 255, 0.6);
            font-size: 0.9em;
            line-height: 1.5;
        }

        .floating-decoration {
            position: fixed;
            border-radius: 50%;
            pointer-events: none;
            opacity: 0.08;
        }

        .deco1 {
            width: 400px;
            height: 400px;
            background: #4a90e2;
            top: -100px;
            left: -100px;
            animation: float 20s ease-in-out infinite;
        }

        .deco2 {
            width: 300px;
            height: 300px;
            background: #d154ff;
            bottom: 100px;
            right: -100px;
            animation: float 25s ease-in-out infinite reverse;
        }

        @keyframes float {
            0%, 100% { transform: translate(0, 0); }
            50% { transform: translate(30px, -30px); }
        }

        @media (max-width: 768px) {
            nav {
                padding: 15px 20px;
                flex-wrap: wrap;
                gap: 15px;
            }

            .nav-brand {
                font-size: 1.4em;
                flex: 1;
            }

            .nav-links {
                gap: 15px;
            }

            .user-info {
                flex-direction: column;
                gap: 10px;
            }

            .main-content {
                padding: 40px 20px;
            }

            .welcome-title {
                font-size: 1.8em;
            }

            .welcome-text {
                font-size: 1em;
            }

            .card-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="floating-decoration deco1"></div>
    <div class="floating-decoration deco2"></div>

    <div class="dashboard">
        <nav>
            <div class="nav-brand">Near<span>Buy</span></div>
            <div class="nav-links">
                <div class="user-info">
                    <div class="user-avatar"><?php echo strtoupper(substr($_SESSION["username"], 0, 1)); ?></div>
                    <div class="username-display"><?php echo htmlspecialchars($_SESSION["username"]); ?></div>
                </div>
                <a href="logout.php" class="logout-btn">Logout</a>
            </div>
        </nav>

        <div class="main-content">
            <div class="welcome-container">
                <div class="welcome-icon">👋</div>
                <h1 class="welcome-title">Welcome, <span class="highlight"><?php echo htmlspecialchars($_SESSION["username"]); ?></span>!</h1>
                <p class="welcome-text">You've successfully logged in to your account. Enjoy exploring all the amazing features we have to offer.</p>

                <div class="card-grid">
                    <div class="dashboard-card">
                        <div class="card-icon">🔒</div>
                        <h3 class="card-title">Secure</h3>
                        <p class="card-text">Your account is protected with industry-standard encryption</p>
                    </div>
                    <div class="dashboard-card">
                        <div class="card-icon">⚡</div>
                        <h3 class="card-title">Fast</h3>
                        <p class="card-text">Lightning-quick performance optimized for your needs</p>
                    </div>
                    <div class="dashboard-card">
                        <div class="card-icon">🎨</div>
                        <h3 class="card-title">Modern</h3>
                        <p class="card-text">Beautiful, intuitive interface designed for you</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>