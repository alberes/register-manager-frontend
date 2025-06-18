function toggleProfileDetails() {
    const details = document.getElementById('profileDetails');
    details.classList.toggle('visible');
}

document.addEventListener('DOMContentLoaded', function() {
    const name = document.getElementById('profileName').textContent.trim();
    const initials = name.split(' ').map(n => n[0]).join('').toUpperCase();
    document.getElementById('profileAvatar').textContent = initials;
});