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
public final class LabServiceGrpc {

  private LabServiceGrpc() {}

  public static final String SERVICE_NAME = "LabService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<lab4.gen.ResultsRecord,
      lab4.gen.BooleanResponse> METHOD_ADD_RESULT =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "LabService", "AddResult"),
          io.grpc.protobuf.ProtoUtils.marshaller(lab4.gen.ResultsRecord.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(lab4.gen.BooleanResponse.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static LabServiceStub newStub(io.grpc.Channel channel) {
    return new LabServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static LabServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new LabServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static LabServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new LabServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class LabServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void addResult(lab4.gen.ResultsRecord request,
        io.grpc.stub.StreamObserver<lab4.gen.BooleanResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ADD_RESULT, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_ADD_RESULT,
            asyncUnaryCall(
              new MethodHandlers<
                lab4.gen.ResultsRecord,
                lab4.gen.BooleanResponse>(
                  this, METHODID_ADD_RESULT)))
          .build();
    }
  }

  /**
   */
  public static final class LabServiceStub extends io.grpc.stub.AbstractStub<LabServiceStub> {
    private LabServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LabServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LabServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LabServiceStub(channel, callOptions);
    }

    /**
     */
    public void addResult(lab4.gen.ResultsRecord request,
        io.grpc.stub.StreamObserver<lab4.gen.BooleanResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ADD_RESULT, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class LabServiceBlockingStub extends io.grpc.stub.AbstractStub<LabServiceBlockingStub> {
    private LabServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LabServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LabServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LabServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public lab4.gen.BooleanResponse addResult(lab4.gen.ResultsRecord request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ADD_RESULT, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class LabServiceFutureStub extends io.grpc.stub.AbstractStub<LabServiceFutureStub> {
    private LabServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LabServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LabServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LabServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<lab4.gen.BooleanResponse> addResult(
        lab4.gen.ResultsRecord request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ADD_RESULT, getCallOptions()), request);
    }
  }

  private static final int METHODID_ADD_RESULT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final LabServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(LabServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ADD_RESULT:
          serviceImpl.addResult((lab4.gen.ResultsRecord) request,
              (io.grpc.stub.StreamObserver<lab4.gen.BooleanResponse>) responseObserver);
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

  private static final class LabServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return lab4.gen.Hospital.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (LabServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new LabServiceDescriptorSupplier())
              .addMethod(METHOD_ADD_RESULT)
              .build();
        }
      }
    }
    return result;
  }
}
