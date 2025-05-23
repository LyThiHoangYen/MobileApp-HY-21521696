import React, { useEffect } from 'react';
import { View, StyleSheet, StatusBar } from 'react-native';
import { SafeAreaProvider, SafeAreaView } from 'react-native-safe-area-context';
import { NavigationContainer } from '@react-navigation/native';

// Component custom
import Title from '../component/OrderConfirm/Title';
import TopTab from '../component/OrderManagement/TopTab';

const OrderManagement = () => {

  // Nếu bạn cần fetch data khi load màn hình thì viết trong useEffect này
  useEffect(() => {
    // console.log('OrderManagement mounted');
    // Có thể đặt logic kiểm tra quyền truy cập, hoặc load đơn hàng mặc định ở đây
  }, []);

  return (
    <SafeAreaProvider>
      {/* SafeAreaView giúp màn hình không bị che bởi tai thỏ, viền camera, status bar */}
      <SafeAreaView style={styles.safeArea}>

        {/* StatusBar setup cho Android & iOS: chỉnh màu thanh trạng thái */}
        <StatusBar barStyle="dark-content" backgroundColor="#ffffff" />

        {/* NavigationContainer để chứa TopTab navigation (TabView đơn hàng) */}
        <NavigationContainer>
          <View style={styles.container}>
            
            {/* Header / Tiêu đề màn hình */}
            <View style={styles.headerSection}>
              <Title />
            </View>

            {/* Nội dung chính: Tab chứa các loại đơn hàng */}
            <View style={styles.contentSection}>
              <TopTab />
            </View>

          </View>
        </NavigationContainer>

      </SafeAreaView>
    </SafeAreaProvider>
  );
};

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#ffffff', // Màu nền trắng nhẹ nhàng
  },
  container: {
    flex: 1,
    paddingHorizontal: 16, // Padding ngang cho toàn màn hình
    backgroundColor: '#f9f9f9', // Nhẹ nhàng hơn màu trắng tinh
  },
  headerSection: {
    paddingTop: 10,
    paddingBottom: 5,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  contentSection: {
    flex: 1,
    paddingTop: 10,
    backgroundColor: '#fff',
    borderRadius: 10,
    overflow: 'hidden', // Giúp bo góc không bị chồng
    elevation: 2, // Bóng nhẹ cho Android
    shadowColor: '#000', // Bóng cho iOS
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
});

export default OrderManagement;
