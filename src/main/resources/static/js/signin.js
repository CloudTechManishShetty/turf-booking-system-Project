const loginButton = document.getElementById("loginButton");
        loginButton.addEventListener('click', (event) => {
            event.preventDefault();

            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            if (!email || !password) {
                console.log("Please fill in all fields.");
                return;
            }

            const data = { email, password };
            const jsonData = JSON.stringify(data);

            fetch('http://localhost:8080/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: jsonData
            })
            .then(response => {
                console.log("Full Response:", response); // Debug: Log the entire response
        
                if (!response.ok) { // Check for non-2xx HTTP status codes first
                    return response.text().then(errorMessage => { // Get error message from server
                        console.error("Server Error:", errorMessage);
                        showToast('errorToast');
                    });
                }
        
                return response.json(); // Parse the JSON response body
            })
            .then(data => {
                console.log("Response Data:", data);
        
                if (data && data.message === "Login successful") {
                    console.log("Shoul redirect to dashboard"); // Redirect on successful login
                    window.location.href = '/dashboard';
                } else {
                    console.error("Login failed:", data ? data.message : "Unexpected response format");
                    showToast('errorToast', data ? data.message : "Login failed");
                }
            })
            .catch(error => {
                console.error('Fetch Error:', error);
                showToast('errorToast');
                console.log("A network error occurred.");
            });

            // Function to show a specific toast
            function showToast(toastId) {
                console.log('Showing toast with ID:', toastId);
                const toastElement = document.getElementById(toastId);
                const toast = new bootstrap.Toast(toastElement);
                toast.show();
            }
        });