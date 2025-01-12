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
                body: jsonData
            })
            .then(response => {
                console.log("Full Response:", response); // Debug: Log the entire response
        
                if (!response.ok) { // Check for non-2xx HTTP status codes first
                    return response.text().then(errorMessage => { // Get error message from server
                        console.error("Server Error:", errorMessage);
                        showToast('errorToast');
                        // Optionally display the error message to the user
                        // alert(errorMessage);
                        // throw new Error("Server returned an error"); // Re-throw to prevent further processing
                    });
                }
        
                return response.json(); // Parse the JSON response body
            })
            .then(data => { // Handle the parsed JSON data (the LoginResponse)
                console.log("Response Data:", data); // Debug: Log the data
        
                if (data && data.token) { // Check if data and token exist
                    const token = data.token;
                    console.log("Token:", token); // Log the token
                    localStorage.setItem('jwtToken', token); // Store the token (e.g., in localStorage)
                    showToast('successToast');
                    window.location.href = '/Home'; // Redirect
                } else {
                    console.error("Token not found in response.");
                    showToast('errorToast');
                    // alert("Token not found in response.");
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
