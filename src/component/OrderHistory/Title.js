import { View, Text, StyleSheet, Dimensions, TouchableOpacity } from 'react-native'
import React from 'react'
import { useFonts } from "expo-font";
import FontAwesome5 from 'react-native-vector-icons/FontAwesome5';
import AppLoading from 'expo-app-loading';

const Title = () => {
    const [fontsLoaded] = useFonts({
        "Roboto-Bold": require("../../assets/fonts/Roboto-Bold.ttf"),
        "Roboto-Medium": require("../../assets/fonts/Roboto-Medium.ttf"),
        "Roboto-Regular": require("../../assets/fonts/Roboto-Regular.ttf"),
    });

    if (!fontsLoaded) {
        return <AppLoading />;
    }

    return (

        <View style={styles.titlebox}>
            <TouchableOpacity style={styles.icon}>
                <FontAwesome5 name="chevron-left" size={20} color="#000000" />
            </TouchableOpacity>
            <View style={styles.title}>
                <Text style={{ fontFamily: "Roboto-Bold", fontSize: 18, }}>Lịch sử đặt hàng</Text>
            </View>
        </View>
    )
};

const deviceWidth = Math.round(Dimensions.get("window").width);

const styles = StyleSheet.create({
    titlebox: {
        backgroundColor: '#FFFFFF',
        padding: 10,
        flexDirection: 'row',
        alignItems: 'center',
        //elevation: 1,
        borderBottomWidth: 1,
        borderColor: '#E5E5E5',
    },
    title: {
        width: deviceWidth,
        justifyContent: 'center',
        //alignItems: 'center',
        //position: 'absolute',
    },
    icon: {
        //position: 'absolute',
        //alignItems: 'flex-start',
        marginHorizontal: 10,
    }
});

export default Title;