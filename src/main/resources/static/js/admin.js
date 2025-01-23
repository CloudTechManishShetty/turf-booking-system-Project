// Section visibility management
function showSection(sectionName) {
    document.getElementById('dashboardSection').classList.add('hidden');
    document.getElementById('bookingsSection').classList.add('hidden');
    document.getElementById('turfsSection').classList.add('hidden');
    document.getElementById(sectionName + 'Section').classList.remove('hidden');
    document.getElementById('sectionTitle').innerText = sectionName.charAt(0).toUpperCase() + sectionName.slice(1);
}

// Bookings management
let bookings = [
    { id: 1, customer: 'John Doe', turf: 'Football Field 1', date: '2023-05-20', time: '14:00-16:00', status: 'Confirmed' },
    { id: 2, customer: 'Jane Smith', turf: 'Cricket Pitch', date: '2023-05-21', time: '10:00-13:00', status: 'Pending' },
    { id: 3, customer: 'Mike Johnson', turf: 'Multi-Sport Arena', date: '2023-05-22', time: '18:00-19:00', status: 'Confirmed' },
];

function renderBookings() {
    const bookingsTableBody = document.getElementById('bookingsTableBody');
    bookingsTableBody.innerHTML = '';
    bookings.forEach(booking => {
        const row = `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${booking.customer}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${booking.turf}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${booking.date}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${booking.time}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${booking.status}</td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <a href="#" class="text-indigo-600 hover:text-indigo-900">Edit</a>
                </td>
            </tr>
        `;
        bookingsTableBody.innerHTML += row;
    });
}

function showTurfForm() {
    document.getElementById('turfFormModal').classList.remove('hidden');
}

function hideTurfForm() {
    document.getElementById('turfFormModal').classList.add('hidden');
    document.getElementById('turfForm').reset();
    document.getElementById('turfId').value = '';
}

function handleTurfSubmit(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const turfData = {
        id: document.getElementById('turfId').value || Date.now(),
        name: formData.get('turfName'),
        type: formData.get('turfType'),
        capacity: parseInt(formData.get('turfCapacity')),
        price: parseFloat(formData.get('turfPrice'))
    };

    const existingTurfIndex = turfs.findIndex(t => t.id == turfData.id);
    if (existingTurfIndex !== -1) {
        turfs[existingTurfIndex] = turfData;
    } else {
        turfs.push(turfData);
    }

    renderTurfs();
    hideTurfForm();
}

function editTurf(id) {
    const turf = turfs.find(t => t.id == id);
    if (turf) {
        document.getElementById('turfId').value = turf.id;
        document.getElementById('turfName').value = turf.name;
        document.getElementById('turfType').value = turf.type;
        document.getElementById('turfCapacity').value = turf.capacity;
        document.getElementById('turfPrice').value = turf.price;
        showTurfForm();
    }
}

function deleteTurf(id) {
    if (confirm('Are you sure you want to delete this turf?')) {
        turfs = turfs.filter(t => t.id != id);
        renderTurfs();
    }
}

let userId = null;
document.getElementById('save').addEventListener('click', async function (event) {
    event.preventDefault(); // Prevent form submission

    // Collect and validate inputs
    const name = document.getElementById('name').value.trim();
    const location = document.getElementById('location').value.trim();
    const pricePerHour = document.getElementById('pricePerHour').value.trim();
    const width = document.getElementById('ground_width').value.trim();
    const length = document.getElementById('ground_length').value.trim();
    const height = document.getElementById('ground_height').value.trim();
    const imageInput = document.getElementById('image');
    const imageFile = imageInput.files[0];

    if (!name || !location || !pricePerHour || !width || !length || !height || !imageFile) {
        alert('All fields are required.');
        return;
    }

    let imageUrl = '';
    if (imageFile) {
        // Step 1: Upload the image and get the URL
        const formData = new FormData();
        formData.append('image', imageFile);

        try {
            const response = await fetch('/api/upload', {
                method: 'POST',
                body: formData,
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText);
            }

            const result = await response.json();
            imageUrl = result.url; // URL of the uploaded image
            console.log(imageUrl);
        } catch (error) {
            console.error('Error uploading image:', error);
            alert('Failed to upload the image. Please try again.');
            return;
        }
    }

    try {
        // Fetch logged-in user
        const userResponse = await fetch('/api/users/me', { method: 'GET', credentials: 'include' });
        if (!userResponse.ok) throw new Error(`User fetch failed: ${await userResponse.text()}`);
        const userData = await userResponse.json();
        const userid = userData.user_id;
        console.log(userid);
        userId = userid;

        const user = await fetch(`/api/users/${userId}`, { method: 'GET', credentials: 'include' });
        if (!user.ok) throw new Error(`User fetch failed: ${await user.text()}`);
        const userD = await user.json();
        console.log(userD);

        // Create FormData to handle file upload
        const formData = new FormData();
        formData.append('name', name);
        formData.append('location', location);
        formData.append('pricePerHour', pricePerHour);
        formData.append('ground_width', width);
        formData.append('ground_length', length);
        formData.append('ground_height', height);
        formData.append('image', imageUrl);
        formData.append('manager', JSON.stringify(userD));

        console.log("This is form Data: ",formData);

        // Create turf
        const turfResponse = await fetch('/api/turfs', {
            method: 'POST',
            credentials: 'include',
            body: formData
        });

        if (!turfResponse.ok) throw new Error(`Turf creation failed: ${await turfResponse.text()}`);

        const result = await turfResponse.json();
        console.log("Turf created successfully:", result);
        alert("Turf successfully created!");
        hideTurfForm();
    } catch (error) {
        console.error('Error:', error);
        alert("There was an issue with your Turf Creation. Please try again.");
    }
});

// Turfs management
document.addEventListener('DOMContentLoaded', function () {
    const userId = 66; // Replace with dynamic userId if needed
    fetch(`/api/users/${userId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // Parse JSON response
        })
        .then(userData => {
            // Access the turfs array
            const turfs = userData.turfs;

            // Update the turf count
            const turfCount = document.getElementById('Turfcnt');
            turfCount.textContent = turfs.length; // Display number of turfs

            // Render turfs in the table
            renderTurfs(turfs);
        })
        .catch(error => {
            console.error('Error:', error);
        });
});

function renderTurfs(turfs) {
    const turfsTableBody = document.getElementById('turfsTableBody');
    turfsTableBody.innerHTML = ''; // Clear existing rows

    turfs.forEach(turf => {
        const row = `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${turf.name}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${turf.location}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${turf.pricePerHour}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${turf.ground_width} x ${turf.ground_length} x ${turf.ground_height}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${new Date(turf.createdAt).toLocaleString()}</td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <button onclick="editTurf(${turf.turf_id})" class="text-indigo-600 hover:text-indigo-900 mr-2">Edit</button>
                    <button onclick="deleteTurf(${turf.turf_id})" class="text-red-600 hover:text-red-900">Delete</button>
                </td>
            </tr>
        `;
        turfsTableBody.innerHTML += row;
    });
}




// // Initial render
// renderBookings();
// renderTurfs();