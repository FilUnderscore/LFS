################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/filesystem/io/lfstypeinputstream.cpp 

OBJS += \
./src/filesystem/io/lfstypeinputstream.o 

CPP_DEPS += \
./src/filesystem/io/lfstypeinputstream.d 


# Each subdirectory must supply rules for building sources it contributes
src/filesystem/io/%.o: ../src/filesystem/io/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


