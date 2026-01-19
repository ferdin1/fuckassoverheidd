const Auth = {
    USER_KEY: 'cjib_user',
    API_URL: '../api', // Correct path van front-end naar api folder

    login: async function (username, password) {
        try {
            const res = await fetch(`${this.API_URL}/login.php`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });
            const data = await res.json();

            if (data.success) {
                const user = {
                    username: data.user.username,
                    role: data.user.role,
                    isLoggedIn: true
                };
                localStorage.setItem(this.USER_KEY, JSON.stringify(user));
                return { success: true, user };
            } else {
                return { success: false, message: data.message };
            }
        } catch (err) {
            console.error("Login Error:", err);
            return { success: false, message: "Verbindingsfout naar de database" };
        }
    },

    register: async function (username, password) {
        try {
            const res = await fetch(`${this.API_URL}/register.php`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });
            const data = await res.json();
            return data;
        } catch (err) {
            console.error("Register Error:", err);
            return { success: false, message: "Verbindingsfout naar de database" };
        }
    },

    logout: function () {
        localStorage.removeItem(this.USER_KEY);
        window.location.href = 'index.html';
    },

    getUser: function () {
        const userStr = localStorage.getItem(this.USER_KEY);
        if (!userStr) return null;
        try { return JSON.parse(userStr); } catch (e) { return null; }
    },

    isLoggedIn: function () {
        return !!this.getUser();
    },

    isAdmin: function () {
        const user = this.getUser();
        return user && user.role === 'admin';
    },

    updateUI: function () {
        const user = this.getUser();
        const roleDisplay = document.getElementById('userRoleDisplay');
        const loginLink = document.getElementById('loginLink');

        if (roleDisplay) {
            roleDisplay.textContent = user ? `Rol: ${user.role}` : 'Rol: Gast';
        }

        if (loginLink) {
            if (user) {
                loginLink.textContent = 'Uitloggen';
                loginLink.onclick = (e) => {
                    e.preventDefault();
                    this.logout();
                };
            } else {
                loginLink.textContent = 'Inloggen';
                loginLink.onclick = (e) => {
                    e.preventDefault();
                    window.location.href = 'login.html';
                };
            }
        }
    }
};

document.addEventListener('DOMContentLoaded', () => {
    Auth.updateUI();
});
