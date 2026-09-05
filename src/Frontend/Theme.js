
(function () {
    var root = document.documentElement;

    function currentTheme() {
        return root.getAttribute('data-theme') ||
            (window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
    }

    function applyTheme(theme) {
        root.setAttribute('data-theme', theme);
        localStorage.setItem('theme', theme);

        document.querySelectorAll('.theme-btn').forEach(function (btn) {
            btn.classList.toggle('active', btn.dataset.themeChoice === theme);
        });
    }

    // sync button state to whatever theme is already active
    // (the inline no-flash script in <head> already set data-theme)
    applyTheme(localStorage.getItem('theme') || currentTheme());

    // wire up every theme button on the page
    document.querySelectorAll('.theme-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            applyTheme(btn.dataset.themeChoice);
        });
    });

    // if the user hasn't picked a theme explicitly, keep following the OS
    // setting live (e.g. their laptop switches to dark mode at sunset)
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', function (e) {
        if (!localStorage.getItem('theme')) {
            applyTheme(e.matches ? 'dark' : 'light');
        }
    });
})();