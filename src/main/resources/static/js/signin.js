const loginButton = document.getElementById("loginButton");
        loginButton.addEventListener('click', (event) => {
            event.preventDefault();

            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            if (!email || !password) {
                alert("Please fill in all fields.");
                return;
            }

            const data = { email, password };
            const jsonData = JSON.stringify(data);

            fetch('http://localhost:8080/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: jsonData
            })
            .then(response => {
                if (response.ok) {
                    console.log('Login successful!');
                    // Redirect to dashboard or another page if necessary
                    window.location.href = '/Home';
                } else {
                    return response.json().then(errorData => {
                        console.error('Error response:', errorData);
                        alert(`Error: ${errorData.message || 'Invalid email or password'}`);
                    });
                }
            })
            .catch(error => {
                console.error('Network error:', error);
                alert('Network error: ' + error.message);
            });
        });