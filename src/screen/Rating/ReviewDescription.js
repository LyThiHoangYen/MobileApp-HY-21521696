import React, { useEffect } from 'react';
import { View, StyleSheet, ScrollView, StatusBar } from 'react-native';

// Import các component đánh giá
import Score from '../../component/Review/Score';
import ReviewDetail from '../../component/Review/ReviewDetail';
import ReviewOverall from '../../component/Review/ReviewOverall';

const ReviewDescription = () => {

  // Hook chạy khi component mount (nếu cần fetch API, init logic...)
  useEffect(() => {
    // Ví dụ: console.log('Review screen loaded');
  }, []);

  return (
    <View style={styles.screen}>
      {/* StatusBar cho Android/iOS */}
      <StatusBar barStyle="dark-content" backgroundColor="#ffffff" />

      {/* Có thể thay View bằng ScrollView nếu nội dung dài */}
      <ScrollView contentContainerStyle={styles.container} showsVerticalScrollIndicator={false}>
        
        {/* Điểm tổng quan (ví dụ: 4.6/5 sao) */}
        <View style={styles.section}>
          <Score />
        </View>

        {/* Tổng hợp các điểm đánh giá sao (đẹp, chất lượng, giao hàng...) */}
        <View style={styles.section}>
          <ReviewOverall />
        </View>

        {/* Danh sách các đánh giá chi tiết */}
        <View style={styles.section}>
          <ReviewDetail />
        </View>

      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: '#fdfdfd',
  },
  container: {
    padding: 16,
    paddingBottom: 32,
  },
  section: {
    marginBottom: 20,
    backgroundColor: '#ffffff',
    borderRadius: 10,
    padding: 12,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.05,
    shadowRadius: 4,
  },
});

export default ReviewDescription;
