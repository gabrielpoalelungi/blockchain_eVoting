import { configureStore, createSlice } from "@reduxjs/toolkit";

const initialState = { value: { username: "", isLogged: false, role: ""} }

const userSlice = createSlice({
    name: "user",
    initialState, 
    reducers: {
        login: (state, action) => {
            state.value = action.payload
        },

        logout: (state, action) => {
            state = initialState
        }
    }
});

export const {login, logout} = userSlice.actions;


export const store = configureStore({
    reducer: {
        user: userSlice.reducer
    }
});