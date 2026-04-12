import React from 'react';

const STATUS_STYLE = {
  FULL: {
    line: 'border-l-red-600',
    badge: 'bg-red-100 text-red-600',
    progress: 'bg-red-600',
  },
  AVAILABLE: {
    line: 'border-l-emerald-600',
    badge: 'bg-emerald-100 text-emerald-600',
    progress: 'bg-emerald-600',
  },
  WARNING: {
    line: 'border-l-amber-700',
    badge: 'bg-amber-100 text-amber-700',
    progress: 'bg-amber-700',
  },
};

function RoomComponent({ room, isDimmed = false }) {
  const statusStyle = STATUS_STYLE[room.status] || STATUS_STYLE.AVAILABLE;
  const occupiedRatio = Math.min(100, (room.patientCount / room.roomCapacity) * 100);
  const availableBeds = Math.max(0, room.roomCapacity - room.patientCount);

  return (
    <article
      className={`w-full rounded-xl border border-gray-200 border-l-4 bg-white p-4 shadow-sm transition ${isDimmed ? 'grayscale opacity-45' : ''} ${statusStyle.line}`}
    >
      <div className="mb-2 flex items-center justify-between">
        <p className="text-3xl font-extrabold leading-none text-gray-800 whitespace-nowrap">{room.roomNumber}호</p>
        <div className="flex items-center gap-2">
          <span className="rounded-md bg-gray-100 px-2 py-1 text-xs font-semibold text-gray-600">
            {room.roomType}
          </span>
          <span className={`rounded-md px-2 py-1 text-xs font-semibold ${statusStyle.badge}`}>
            {room.status}
          </span>
        </div>
      </div>

      <p className="mb-2 text-sm text-gray-600">
        성별: {room.gender}{' '}
        <span className="ml-2 font-semibold text-emerald-700">
          {room.patientCount}/{room.roomCapacity} Beds
        </span>
      </p>

      <div className="mb-3 h-2 w-full overflow-hidden rounded-full bg-gray-200">
        <div className={`h-full rounded-full ${statusStyle.progress}`} style={{ width: `${occupiedRatio}%` }} />
      </div>

      <div className="flex items-center justify-between">
        <div className="flex gap-1.5">
          {(room.nurses || []).map((nurse) => (
            <span
              key={nurse}
              className="flex h-6 w-6 items-center justify-center rounded-full bg-gray-200 text-xs font-semibold text-gray-600"
            >
              {nurse}
            </span>
          ))}
        </div>
        <p className="text-xs text-gray-500">{availableBeds} Available</p>
      </div>
    </article>
  );
}

export default RoomComponent;