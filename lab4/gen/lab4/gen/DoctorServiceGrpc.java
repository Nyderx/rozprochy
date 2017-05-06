package lab4.gen;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.3.0)",
    comments = "Source: hospital.proto")
public final class DoctorServiceGrpc {

  private DoctorServiceGrpc() {}

  public static final String SERVICE_NAME = "DoctorService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<lab4.gen.DoctorQuery,
      lab4.gen.ResultsRecord> METHOD_GET_ALL =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING,
          generateFullMethodName(
              "DoctorService", "GetAll"),
          io.grpc.protobuf.ProtoUtils.marshaller(lab4.gen.DoctorQuery.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(lab4.gen.ResultsRecord.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DoctorServiceStub newStub(io.grpc.Channel channel) {
    return new DoctorServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DoctorServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new DoctorServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static DoctorServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new DoctorServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class DoctorServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getAll(lab4.gen.DoctorQuery request,
        io.grpc.stub.StreamObserver<lab4.gen.ResultsRecord> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_ALL, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_GET_ALL,
            asyncServerStreamingCall(
              new MethodHandlers<
                lab4.gen.DoctorQuery,
                lab4.gen.ResultsRecord>(
                  this, METHODID_GET_ALL)))
          .build();
    }
  }

  /**
   */
  public static final class DoctorServiceStub extends io.grpc.stub.AbstractStub<DoctorServiceStub> {
    private DoctorServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DoctorServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DoctorServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DoctorServiceStub(channel, callOptions);
    }

    /**
     */
    public void getAll(lab4.gen.DoctorQuery request,
        io.grpc.stub.StreamObserver<lab4.gen.ResultsRecord> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_GET_ALL, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class DoctorServiceBlockingStub extends io.grpc.stub.AbstractStub<DoctorServiceBlockingStub> {
    private DoctorServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DoctorServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DoctorServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DoctorServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<lab4.gen.ResultsRecord> getAll(
        lab4.gen.DoctorQuery request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_GET_ALL, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class DoctorServiceFutureStub extends io.grpc.stub.AbstractStub<DoctorServiceFutureStub> {
    private DoctorServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DoctorServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DoctorServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DoctorServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_GET_ALL = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DoctorServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DoctorServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ALL:
          serviceImpl.getAll((lab4.gen.DoctorQuery) request,
              (io.grpc.stub.StreamObserver<lab4.gen.ResultsRecord>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class DoctorServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return lab4.gen.Hospital.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DoctorServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DoctorServiceDescriptorSupplier())
              .addMethod(METHOD_GET_ALL)
              .build();
        }
      }
    }
    return result;
  }
}
