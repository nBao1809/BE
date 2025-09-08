// Auto-generated entity types for all entities in the system
// Status fields use types from entity-status.types.ts

export type Category = {
  id: number;
  name: string;
};

export type Customer = {
  id: string;
  name: string;
  phone: string;
  loyaltyPoints: number;
};
public class CategoryWithDishesDTO {
    public Long id;
    public String name;
    public List<DishShortDTO> dishes;

    public static class DishShortDTO {
        public Long id;
        public String name;
        public BigDecimal price;
        public String imageUrl;
    }
export type MenuCategory = {
  id: number;
  name: string;
  dishes: Dish[];
};
export type OptionType = {
  id: number;
  content: string;
};

export type OptionDetail = {
  id: number;
  optionTypeId: OptionType;
  content: string;
};

export type Dish = {
  id: number;
  name: string;
  price: number;
  imageUrl: string;
  status: DishStatus;
  category: Category;
  options: OptionDetail[];
};

export type Employee = {
  id: string;
  username: string;
  password: string;
  fullName: string;
  role: string;
};

export type Session = {
  id: string;
  table: Tables;
  customer: Customer;
  startTime: string;
  endTime: string;
  status: SessionStatus;
};

export type Feedback = {
  id: number;
  session: Session;
  rating: number;
  content: string;
  createdAt: string;
};

export type Orders = {
  id: string;
  session: Session;
  amount: number;
  loyaltyRedeemPoints: number;
  discountAmount: number;
  createdAt: string;
  totalAmount: number;
  status: OrdersStatus;
  customer: Customer;
};

export type OrderDetail = {
  id: number;
  order: Orders;
  dish: Dish;
  quantity: number;
  note: string;
  status: OrderDetailStatus;
};

export type Payment = {
  id: string;
  order: Orders;
  amount: number;
  method: string;
  status: PaymentStatus;
  transactionCode: string;
  paidAt: string;
  cashier: Employee;
};

export type ServiceType = {
  id: number;
  typeName: string;
  description: string;
};

export type ServiceRequest = {
  id: number;
  session: Session;
  serviceType: ServiceType;
  created_at: string;
  handleTime: string;
  status: ServiceRequestStatus;
};

export type Tables = {
  id: number;
  number: number;
  status: TableStatus;
};

// Status types for all entities with a status field
export type DishStatus = 'AVAILABLE' | 'UNAVAILABLE';
export type OrderDetailStatus = 'PENDING' | 'COOKING' | 'DONE' | 'SERVED'| 'CANCELLED';
export type OrdersStatus ='ORDERING'| 'WAITING_PAYMENT' | 'PAID';
export type PaymentStatus = 'PAID' | 'PENDING' | 'FAILED';
export type ServiceRequestStatus = 'PENDING'| 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
export type SessionStatus = 'OPEN' | 'ENDED';
export type TableStatus = 'OCCUPIED' | 'AVAILABLE';
