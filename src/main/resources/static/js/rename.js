document.addEventListener('DOMContentLoaded', () => {
    const renameButton = document.querySelectorAll('.rename-btn');
    renameButton.forEach(button => {
        button.addEventListener('click', (event) => {
            const row = event.target.closest('tr');
            const renameForm = row.querySelector('.rename-form');
            const fileNameDisplay = row.querySelector('.file-name');
            const renameInput = renameForm.querySelector('.rename-input');

            // Toggle visibility of the rename form and file name display
            fileNameDisplay.classList.add('d-none');
            renameForm.classList.remove('d-none');

            // Pre-fill the input with the current file name
            renameInput.focus();
            renameInput.select();
        });
    });
    // Handle Save Button Click
    const saveButtons = document.querySelectorAll('.rename-form button[type="submit"]');
    saveButtons.forEach(button => {
        button.addEventListener('click', (event) => {
            event.preventDefault(); // Prevent form submission for validation

            const form = event.target.closest('form');
            const renameInput = form.querySelector('.rename-input');
            const originalName = form.querySelector('[name="path"]').value; // Fetch the original name
            const fileNameDisplay = form.closest('tr').querySelector('.file-name');
            console.log("renameInput:", renameInput.value.trim());
            console.log("originalName:", originalName.trim());

            // Check if the input value hasn't changed
            if (renameInput.value.trim() === originalName.trim()) {
                event.preventDefault();
                form.classList.add('d-none');
                fileNameDisplay.classList.remove('d-none');
                return;
            }
            form.submit();
        });
    });
});