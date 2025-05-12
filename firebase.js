import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getAuth, onAuthStateChanged } from "firebase/auth";
import {
  getFirestore,
  collection,
  collectionGroup,
  addDoc,
  getDoc,
  getDocs,
  setDoc,
  updateDoc,
  doc,
  onSnapshot,
  query,
  where,
} from "firebase/firestore";
import { getStorage } from "firebase/storage";

const firebaseConfig = {
  apiKey: "AIzaSyD26IWqBX0o-rfDyla2efK-p60t3WvL4KI",
  authDomain: "hynfoodapp.firebaseapp.com",
  projectId: "hynfoodapp",
  storageBucket: "hynfoodapp.appspot.com",
  messagingSenderId: "648050555142",
  appId: "1:648050555142:web:9e1419657ea6fed037c687",
  measurementId: "G-52VJNNLEN9"
};

const app = initializeApp(firebaseConfig);
//const analytics = getAnalytics(app);
const auth = getAuth(app);
const db = getFirestore(app);
const storage = getStorage(app);

export {
  auth, app, db, storage,
  collection, collectionGroup, addDoc,
  getDoc, getDocs, setDoc, updateDoc,
  doc, onSnapshot, query, where
};
