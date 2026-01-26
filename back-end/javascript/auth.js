const Auth = {
    USER_KEY: 'cjib_user',
    API_URL: '../api', // Correct path van front-end naar api folder

    // TEST ACCOUNTS (lokaal in localStorage)
    TEST_ACCOUNTS: {
        'admin': { password: 'admin', role: 'admin' },
        'editor': { password: 'editor123', role: 'editor' }
    },

    login: async function (username, password) {
        try {
            // TEST MODE: Check tegen lokale accounts
            // if (this.TEST_ACCOUNTS[username]) {
            //     if (this.TEST_ACCOUNTS[username].password === password) {
            //         const user = {
            //             username: username,
            //             role: this.TEST_ACCOUNTS[username].role,
            //             isLoggedIn: true
            //         };
            //         localStorage.setItem(this.USER_KEY, JSON.stringify(user));
            //         console.log('✅ Test login succesvol!');
            //         return { success: true, user };
            //     } else {
            //         return { success: false, message: "Wachtwoord onjuist" };
            //     }
            // }

            // NORMALE MODE: Probeer database
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
            // NORMALE MODE: Registreer via API
            const res = await fetch(`${this.API_URL}/register.php`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });
            return await res.json();
        } catch (err) {
            console.error("Register Error:", err);
            return { success: false, message: "Registratie mislukt" };
        }
    },

    logout: function () {
        localStorage.removeItem(this.USER_KEY);
        window.location.href = 'dashboard.html';
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

console.log('🧪 TEST MODE AKTIEF - Gebruik: admin/admin of editor/editor123');

document.addEventListener('DOMContentLoaded', () => {
    Auth.updateUI();
});
