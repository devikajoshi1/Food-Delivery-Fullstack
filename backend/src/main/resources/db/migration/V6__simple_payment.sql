ALTER TABLE orders ADD COLUMN payment_method VARCHAR(20);

ALTER TABLE orders DROP COLUMN razorpay_order_id;
ALTER TABLE orders DROP COLUMN razorpay_payment_id;